package com.copart.figs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class VehicleAuctionResult {

	@JsonIgnore
	private String lotNumber;
	
	@JsonProperty("yard_num")
	private String facilityNumber;
	
	@JsonProperty("member_num")
	private String memberNumber;
	
	@JsonProperty("high_bid_amount")
	private Double highBidAmount;
	
	@JsonProperty("bid_histories")
	private List<VehicleBid> bidHistory = new ArrayList<VehicleBid>();
	
	@JsonProperty("warning_flag")
	private String warningFlag;
	
	@JsonProperty("warning_message")
	private String warningMessage;
	
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getFacilityNumber() {
		return facilityNumber;
	}
	public void setFacilityNumber(String facilityNumber) {
		this.facilityNumber = facilityNumber;
	}
	public String getMemberNumber() {
		return memberNumber;
	}
	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}
	public Double getHighBidAmount() {
		return highBidAmount;
	}
	public void setHighBidAmount(Double highBidAmount) {
		this.highBidAmount = highBidAmount;
	}
	public List<VehicleBid> getBidHistory() {
		return bidHistory;
	}
	public void setBidHistory(List<VehicleBid> bidHistory) {
		this.bidHistory = bidHistory;
	}
	public String getWarningFlag() {
		return warningFlag;
	}
	public void setWarningFlag(String warningFlag) {
		this.warningFlag = warningFlag;
	}
	public String getWarningMessage() {
		return warningMessage;
	}
	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	
}
