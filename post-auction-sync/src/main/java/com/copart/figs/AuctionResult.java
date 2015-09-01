package com.copart.figs;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;



public class AuctionResult {

	@JsonIgnore
	private Long auctionLotOutboundId;
	@JsonIgnore
	private String countryCode;
	@JsonProperty("api_key")
	private String apiKey;
	@JsonProperty("vehicle")
	private VehicleAuctionResult vehicle;
	
	private String  auctionServiceOrderStatus;
	
	public String getAuctionServiceOrderStatus() {
		return auctionServiceOrderStatus;
	}
	public void setAuctionServiceOrderStatus(String auctionServiceOrderStatus) {
		this.auctionServiceOrderStatus = auctionServiceOrderStatus;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public VehicleAuctionResult getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleAuctionResult vehicle) {
		this.vehicle = vehicle;
	}
	public Long getAuctionLotOutboundId() {
		return auctionLotOutboundId;
	}
	public void setAuctionLotOutboundId(Long auctionLotOutboundId) {
		this.auctionLotOutboundId = auctionLotOutboundId;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
