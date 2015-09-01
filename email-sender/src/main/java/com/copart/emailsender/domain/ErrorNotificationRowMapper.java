package com.copart.emailsender.domain;


import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ErrorNotificationRowMapper implements RowMapper{

	
	@Override
	public Object mapRow(ResultSet rs, int i) throws SQLException {
		ErrorNotificationRecord record = new ErrorNotificationRecord();
		record.setMessageNotificationId(rs.getLong("MESSAGES_NOTIFICATION_ID"));
		record.setCompanyCode(rs.getString("COMPANY_CODE"));
		record.setNotificationDesc(rs.getString("NOTIFICATION_DESC"));
		record.setProcessedFlag(rs.getString("PROCESSED_FLAG"));
		record.setProcessedMessage(rs.getString("PROCESSED_MESSAGE"));
		record.setSourceTableName(rs.getString("SOURCE_TABLE_NAME"));
		record.setSourceIdColumnName(rs.getString("SOURCE_ID_COLUMN_NAME"));
		record.setSourceDataList(rs.getString("SOURCE_DATA_LIST"));
		
		try {
			record.setMessageProcessedBy(InetAddress.getLocalHost().getHostName());			
		} catch (Exception e) {

		}
		return record;
	}
}
