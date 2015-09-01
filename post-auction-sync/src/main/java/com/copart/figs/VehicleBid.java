package com.copart.figs;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class VehicleBid {

	@JsonProperty("lot_num")
	private String lotNumber;

	@JsonProperty("bid_type_code")
	private String bidTypeCode;

	@JsonProperty("bid_source_code")
	private String bidSourceCode;
	
	@JsonIgnore
	private Long auctionDate;
	
	@JsonProperty("member_num")
	private String memberNumber;

	@JsonProperty("bid_amount")	
	private Double bidAmount;
	
	@JsonProperty("bid_time_timestamp")
	private Long bidTime;
	
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getBidTypeCode() {
		return bidTypeCode;
	}
	public void setBidTypeCode(String bidTypeCode) {
		this.bidTypeCode = bidTypeCode;
	}
	public Long getAuctionDate() {
		return auctionDate;
	}
	public void setAuctionDate(Long auctionDate) {
		this.auctionDate = auctionDate;
	}
	public String getMemberNumber() {
		return memberNumber;
	}
	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}
	public Double getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(Double bidAmount) {
		this.bidAmount = bidAmount;
	}
	public Long getBidTime() {
		return bidTime;
	}
	public void setBidTime(Long bidTime) {
		this.bidTime = bidTime;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public String getBidSourceCode() {
		return bidSourceCode;
	}
	public void setBidSourceCode(String bidSourceCode) {
		this.bidSourceCode = bidSourceCode;
	}
}
