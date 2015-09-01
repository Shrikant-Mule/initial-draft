package com.copart.figs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


@Component
public class AuctionResultMapper implements RowMapper<AuctionResult> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public AuctionResult mapRow(ResultSet rs, int i) throws SQLException {
		

		VehicleAuctionResult vehicle = new VehicleAuctionResult();
		AuctionResult result = new AuctionResult();
		result.setCountryCode("DE");
		result.setApiKey("c0partation");
		result.setVehicle(vehicle);
		result.setAuctionLotOutboundId(rs.getLong("AUCTION_LOT_OUTBOUND_ID"));
		result.setAuctionServiceOrderStatus(rs.getString("AUCTION_SERVICE_ORDER_STATUS"));
		
		vehicle.setFacilityNumber(StringUtils.trim((rs.getString("FACILITY_NUMBER"))));
		vehicle.setLotNumber(StringUtils.trim(rs.getString("LOT_NUMBER")));
		vehicle.setMemberNumber(StringUtils.trim(rs.getString("HIGH_BIDDER_1")));
		vehicle.setHighBidAmount(rs.getDouble("BID_AMOUNT_1"));
		vehicle.setWarningFlag(rs.getString("WARNING_FLAG"));
		vehicle.setWarningMessage(StringUtils.trim(rs.getString("WARNING_MESSAGE")));
		
		//vehicle.setBidHistory(jdbcTemplate.query("select * from proton.BID where LOT_NUMBER = ? order by bid_amount desc", new Object[] {vehicle.getLotNumber()}, new RowMapper<VehicleBid>() {
		// changed the query to fix IGS-994
		
		vehicle.setBidHistory(jdbcTemplate.query("select B.* from BID B join LOT L on L.CURRENT_AUCTION_ID = B.AUCTION_ID AND L.LOT_ID = B.LOT_ID where L.LOT_NUMBER = ? order by bid_amount desc", new Object[] {vehicle.getLotNumber()}, new RowMapper<VehicleBid>() {
			@Override
			public VehicleBid mapRow(ResultSet rst, int index) throws SQLException {
				VehicleBid bid = new VehicleBid();
				bid.setLotNumber(rst.getString("LOT_NUMBER"));
				bid.setMemberNumber(StringUtils.trim(rst.getString("BUYER_NUMBER")));
				bid.setBidTypeCode(StringUtils.trim(rst.getString("BID_TYPE_CODE")));
				bid.setBidSourceCode(StringUtils.trim(rst.getString("BID_SOURCE_CODE")));
				bid.setBidAmount(rst.getDouble("BID_AMOUNT"));
				Calendar utccal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				Date ts = rst.getDate("BID_TIMESTAMP", utccal);
				bid.setBidTime(ts.getTime() / 1000);
				return bid;
			}
		}));
		
		
		return result;
	}

}
