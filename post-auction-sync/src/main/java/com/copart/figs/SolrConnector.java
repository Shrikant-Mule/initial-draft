package com.copart.figs;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class SolrConnector
{
    private static final String SELLER_BILLING = "70";
    
	Logger logger = Logger.getLogger(SolrConnector.class);
    @Value("${solr.lotcore.url}")
    private String solrLotCoreUrl;

    @Value("${solr.lotcore.connection.timeout}")
    private int solrLotCoreConnectionTimeout;

    @Value("${solr.lotcore.response.timeout}")
    private int solrLotCoreResponseTimeout;

    @Value("${solr.lotcore.max.retries}")
    private int solrLotCoreMaxRetries;
    
   
    Gson gson=new Gson();
         
    public boolean send(AuctionResult auctionResult) throws SolrServerException, IOException, URISyntaxException
    {
    	HttpSolrServer server = null;
        try
        {
            logger.info("Updating solr data. URL : " + solrLotCoreUrl + " Connection timeout : " + solrLotCoreConnectionTimeout + " Response Timeout : " +solrLotCoreResponseTimeout + " MaxRetries : " + solrLotCoreMaxRetries);
            
            server = new HttpSolrServer(solrLotCoreUrl);

            server.setConnectionTimeout(solrLotCoreConnectionTimeout);
            server.setSoTimeout(solrLotCoreResponseTimeout);
            server.setMaxRetries(solrLotCoreMaxRetries);  
            server.setParser(new XMLResponseParser()); // binary parser is used
                                                       // by default
            server.setFollowRedirects(false);
            server.setAllowCompression(false);
            UpdateResponse rsp;

            SolrInputDocument doc = new SolrInputDocument();
                    
            //doc.addField("ID", auctionResult.getVehicle().getLotNumber());
            doc.addField("LOT_NUMBER", auctionResult.getVehicle().getLotNumber());
            //doc.addField("YARD_NUMBER", auctionResult.getVehicle().getFacilityNumber());
            HashMap<String,String> memberUpdateMap = new HashMap<String,String>(); 
            memberUpdateMap.put("set", auctionResult.getVehicle().getMemberNumber());            
            doc.addField("MEMBER_ID",memberUpdateMap);
            
            HashMap<String,String> lotStageMap = new HashMap<String,String>(); 
            lotStageMap.put("set", SELLER_BILLING);
            doc.addField("LOT_STAGE", lotStageMap);
            
            HashMap<String,String> highBidMap = new HashMap<String,String>(); 
            highBidMap.put("set", auctionResult.getVehicle().getHighBidAmount()+"");
            doc.addField("HIGH_BID", highBidMap);
            
            
            logger.info("Document Object : " + doc.toString());
            
            rsp = server.add(doc);
            server.commit(); // periodically flush

            logger.info(" REcord Updated into solr for lot # " + auctionResult.getVehicle().getLotNumber() );
            
            if (rsp.getStatus() == 0)
            {
                logger.info("Successfully added lot#" + auctionResult.getVehicle().getLotNumber() + " Solr");
                return true;
            }
            else
            {
                logger.info("SOLR Update Status Status: " + rsp.getStatus());
            }


        }
        catch (SolrException e)
        {
            // TODO: handle exception
            logger.info("*************Solr Exception: " + e.getMessage(),e);
        }
        catch (IOException e)
        {
            // TODO: handle exception
            logger.info("*************IO Exception: " + e.getMessage(),e);
        }
        finally
        {
            /*if(server!=null)
            {
                try
                {
                    server.getHttpClient();
                }
                catch(IOException ioe)
                {
                    //do nothing
                }
            }*/
        }

        return false;
    }

}