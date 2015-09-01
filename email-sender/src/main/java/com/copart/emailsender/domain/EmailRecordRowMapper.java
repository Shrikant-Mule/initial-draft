package com.copart.emailsender.domain;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.copart.emailsender.util.EmailSenderConstants;

@Component
public class EmailRecordRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int i) throws SQLException {
		EmailRecord record = new EmailRecord();
		record.setEmailId(rs.getLong(EmailSenderConstants.EMAIL_ID));
		record.setCompanyCode(rs.getString(EmailSenderConstants.COMPANY_CODE));
		record.setPreferredLanguage(rs.getString(EmailSenderConstants.PREFERRED_LANGUAGE));
		record.setTemplateId(rs.getLong(EmailSenderConstants.EMAIL_TEMPLATE_ID));
		record.setTemplateName(rs.getString(EmailSenderConstants.TEMPLATE));
		record.setSubject(rs.getString(EmailSenderConstants.SUBJECT));
		record.setTo(rs.getString(EmailSenderConstants.TO_ADDRESS));
		try {
			record.setProcessedBy(InetAddress.getLocalHost().getHostName());			
		} catch (Exception e) {

		}
		return record;
	}

}
