package com.copart.figs;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.copart.service.domain.Lot;
import com.copart.service.domain.Member;
import com.couchbase.client.CouchbaseClient;
import com.google.gson.Gson;

@Component
public class AuctionResultsProcessor {
	

	private static final Short SELLER_BILLING = 70;

	private static Logger logger = Logger.getLogger(AuctionResultsProcessor.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ApplicationConfiguration configuration;
	
	@Autowired
	private PostAuctionCouchbaseRepository couchbaseRepository;
	
	@Autowired
	private CouchbaseCacheManager cacheManager;
	
    @Value("${couchbase.bucketName}")
    private String bucket;
    
	private RestTemplate restTemplate;
	
	@Autowired
	private SolrConnector solrConnector;
	
	public AuctionResultsProcessor() {
		HttpComponentsClientHttpRequestFactory factory=new HttpComponentsClientHttpRequestFactory();
		//factory.setBufferRequestBody(false);
		this.restTemplate = new RestTemplate(factory);
	}
	
	public void sendAuctionResult(AuctionResult result) {


		try {
			logger.info("Auction Result : " + result);
			HttpHeaders requestHeaders = new HttpHeaders();
			
			MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));		
			requestHeaders.setContentType(mediaType);
			ArrayList charSetList= new ArrayList();
			charSetList.add(StandardCharsets.UTF_8);
			requestHeaders.setAcceptCharset( charSetList);
			
			
	        
			HttpEntity<?> requestEntity = new HttpEntity<Object>(result,  requestHeaders);
			//requestHeaders.setContentLength(requestEntity.getBody().toString().length());
			logger.info("***** Calling Cobalt web service to post the auction data.");
			ResponseEntity<String> response = restTemplate.exchange(configuration.getFigsUrl() + "" + result.getVehicle().getLotNumber() + ".json", 
																	HttpMethod.PUT, requestEntity, String.class);
			logger.info("####### Cobalt web service call result. Response Status : " + response.getStatusCode().value());

			String status = null;
		
			if (response.getStatusCode().value() >= 200 && response.getStatusCode().value() < 300) {
				status = "P";
			}
			else {
				status = "E";
			}
			logger.debug("Cobalt Response is : " + response.getStatusCode() + response.getBody());
			
			jdbcTemplate.update(configuration.getUpdate(), 
					status, StringUtils.left(response.getBody(), 200), result.getAuctionLotOutboundId());
			
			//Pushing to Couchbase and solr if lot is pure sale.
			if("P".equals(status) && "AU07".equalsIgnoreCase(result.getAuctionServiceOrderStatus()) ){
			  
		    	logger.info("Updating couchbase for lot # : " + result.getVehicle().getLotNumber() );
		    	sendAuctionResultToCouchbase(result);
		    	logger.info("Updated couchbase successfully for lot # : " + result.getVehicle().getLotNumber() );
 
				
			    logger.info("Updating SOLR for lot # : " + result.getVehicle().getLotNumber() );
			    solrConnector.send(result);
			    logger.info("Updated SOLR successfully for lot # : " + result.getVehicle().getLotNumber() );
			}
			
		
					
		}
		
		catch (HttpClientErrorException e) {
			String message = e.getStatusCode().value() + " " +  e.getStatusText() + " " + e.getResponseBodyAsString();
			logger.error("Error while calling Cobalt web service: " + message);
			jdbcTemplate.update(configuration.getUpdate(), 
					"E", StringUtils.left(message, 200), result.getAuctionLotOutboundId());
		}
		catch (HttpServerErrorException e) {
			String message = e.getStatusCode().value() + " " +  e.getStatusText() + " " + e.getResponseBodyAsString();
			logger.error("Error while calling Cobalt web service: " + message);
			jdbcTemplate.update(configuration.getUpdate(), 
					"E", StringUtils.left(message, 200), result.getAuctionLotOutboundId());
		}
		catch (ResourceAccessException e){
			logger.error("ResourceAccessException: Error sending data to FIGS - going to retry " , e);
			String message = e.getMessage().toString() + " " +  e.getCause() ;
			logger.error("Error while calling Cobalt web service: " + message);
			jdbcTemplate.update(configuration.getUpdate(), 
					"N", StringUtils.left(message, 200), result.getAuctionLotOutboundId());
		}
	    catch (CouchbaseUpdateException e){
	            logger.error("CouchbaseUpdateException: Error sending data to Couchbase" , e);
	            String message = e.getMessage().toString() + " " +  e.getCause() ;
	            logger.error("Error while calling Cobalt web service: " + message);
	        }
		catch (Exception e) {
			logger.error("Error sending data to FIGS - going to retry " , e);

			jdbcTemplate.update(configuration.getUpdate(), 
					"N", StringUtils.left(e.getMessage(), 200), result.getAuctionLotOutboundId());
		}
		
	}
	

	private void sendAuctionResultToCouchbase(AuctionResult result) throws CouchbaseUpdateException {
        logger.debug("Pushing to Couchbase : " + result);
        VehicleAuctionResult vehicle = result.getVehicle();
        Lot lot = couchbaseRepository.findOne(vehicle.getLotNumber());
        /*Yard yard = lot.getYard();
        yard.setNumber(vehicle.getFacilityNumber());
        lot.setYard(yard);*/        
        Member member = lot.getMember();
        member.setMemberId(Long.parseLong(vehicle.getMemberNumber()));
        lot.setMember(member);
        lot.setHighestBidAmount(vehicle.getHighBidAmount());
        lot.setLotStage(SELLER_BILLING);
        lot.setLotNumber(Long.parseLong(vehicle.getLotNumber()));
        Gson gson=new Gson();
        String lotToUpdate = gson.toJson(lot);
        sendAuctionResultToCouch(lotToUpdate, vehicle.getLotNumber() );
        
    }
    
    private void sendAuctionResultToCouch(String lotToUpdate, String id) throws CouchbaseUpdateException{
        try{
            CouchbaseClient couchbaseClient = cacheManager.getClients().get(bucket);
            couchbaseClient.replace(id, lotToUpdate);
            logger.debug("Data Updated in Couchbase for lot Number : " + id);
            
        } catch(Exception ex){
            throw new CouchbaseUpdateException(ex);
        }
        
    }
    
}
