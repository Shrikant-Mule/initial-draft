package com.copart.emailsender.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.copart.emailsender.domain.EmailRecord;
import com.copart.emailsender.domain.ErrorNotificationRecord;
import com.copart.emailsender.services.EmailService;

public abstract class EmailServiceImpl implements EmailService {

	
	
	@Autowired
	private JdbcTemplate template;
		
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Value("${mail.from}")
	protected String fromEmail;
	
	@Value("${mail.alternate.to}")
	protected String alternateEmail;
	
	@Value("${mail.alternate.errorNotification.to}")
	protected String alternateErrorNotificationEmail;
	
	@Value("${email.default.language}")
	protected String defaultLanguage;
	
	/*@Value("${email.templates.location}")
	protected String templatesLocation;*/
	
	@Value("${email.subject}")
	String mailSubject;
	
	@Value("${email.param.query}")
	String paramSelectQuery;
	
	@Value("${email.notification.sent.query}")
	String notificationProcessedQuery;
	
	@Value("${email.notification.tempalte.name}")
	String notificationTemplateName;

	
	@Value("${email.errorNotificationDL}")
	String errorNotificationDL;
		
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public abstract void sendEmail(EmailRecord record) throws Exception;
	
	@Override
	public void processEmail(EmailRecord record) {
		try {
			Map<String, Object> params = template.query("select variable_key, variable_value from email_detail where email_id = ?", new ResultSetExtractor<Map<String, Object>>() {
				@Override
				public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<String, Object> params = new HashMap<String, Object>();
					while (rs.next()) {
						params.put(rs.getString("VARIABLE_KEY"), rs.getObject("VARIABLE_VALUE"));
					}
					return params;
				}
			}, record.getEmailId());
			params.put("TO_EMAIL", record.getTo());
			params.put("FROM_EMAIL", fromEmail);
			//if (StringUtils.isNotBlank(bccEmail)) {
			//	params.put("ORIGINAL-EMAIL", "<tr>DEV/QA Environment. Original destination: <h2>" + record.getTo() + "</h2></tr>");
			//}
			record.setParameters(params);
			String prefLang = defaultLanguage.toUpperCase();
			//Checking email template language
			if(record.getPreferredLanguage() != null && !"".equals(record.getPreferredLanguage().trim())) {
				prefLang = record.getPreferredLanguage().toUpperCase();
			}
			//Relative path to template
			String templateName = record.getCompanyCode().trim()+"/"+prefLang.trim()+"/"+record.getTemplateName() + ".vm";
			Map<String,Object> recordParams = record.getParameters();
			recordParams.put("COMMONPATH", record.getCompanyCode().trim()+"/"+prefLang.trim());
			record.setEmailBody(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, "UTF-8", recordParams));
			record.setSubject( replaceVariables(record.getSubject(), params));
			logger.debug("Sending " + record.getTemplateName() + " to: " + record.getTo());
			record.setAlternateTo(alternateEmail);
			sendEmail(record);
			record.setProcessedFlag("P");
		}
		catch (Exception e) {
			logger.error(e);
			record.setProcessedFlag("E");
			record.setMessage(e.getMessage());
		}
		markEmailProcessed(record);
	}
	
	
	@Override
	public void processEmailNotification(ErrorNotificationRecord record) {
		try {
			Map<String, Object> params = template.query(paramSelectQuery, new ResultSetExtractor<Map<String, Object>>() {
				@Override
				public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<String, Object> params = new HashMap<String, Object>();
					List<String> listOfVariableForFailure = new ArrayList<String>();
					while (rs.next()) {
						//params.put(rs.getString("KEY_COLUMN_NAME"), rs.getObject("KEY_COLUMN_VALUE"));
						listOfVariableForFailure.add(rs.getString("KEY_COLUMN_NAME") + "  :  " + rs.getObject("KEY_COLUMN_VALUE"));
					}
					params.put("listOfVariableForFailure", listOfVariableForFailure);
					return params;
				}
			}, record.getMessageNotificationId()); 
			params.put("TO_EMAIL", errorNotificationDL);
			params.put("FROM_EMAIL", fromEmail);
			record.setParameters(params);
			String prefLang = defaultLanguage.toUpperCase();
			
			params.put("NOTIFICATION_DESC", record.getNotificationDesc());
			params.put("PROCESSED_MESSAGE", record.getProcessedMessage());
			params.put("COMPANY_CODE", record.getCompanyCode());
			//Checking email template language
			/*if(record.getPreferredLanguage() != null && !"".equals(record.getPreferredLanguage().trim())) {
				prefLang = record.getPreferredLanguage().toUpperCase();
			}*/
			//Relative path to template
			String templateName = record.getCompanyCode().trim()+"/"+prefLang.trim()+"/"+ notificationTemplateName + ".vm";
			Map<String,Object> recordParams = record.getParameters();
			recordParams.put("COMMONPATH", record.getCompanyCode().trim()+"/"+prefLang.trim());
			record.setEmailBody(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, "UTF-8", recordParams));
			record.setSubject( replaceVariables(mailSubject, params));
			record.setAlternateTo(alternateErrorNotificationEmail);
			record.setTo(errorNotificationDL);
			record.setTemplateName(notificationTemplateName);
			sendEmail(record);
			record.setMessageProcessedFlag("P");
		}
		catch (Exception e) {
			logger.error(e);
			record.setMessageProcessedFlag("E");
			//record.setMessage(e.getMessage());
		}
		markErrorNotificationProcessed(record);
	}
	public String replaceVariables(String template, Map<String, Object> vars) {
		for (String key: vars.keySet()) {
			template = StringUtils.replace(template, "$" + key, String.valueOf(vars.get(key)));
		}
		return template;
	}
	
	public void markEmailProcessed(EmailRecord record) {
		template.update("update email set processed_flag = ?, processed_by = ?  where email_id = ?", record.getProcessedFlag(), record.getProcessedBy(), record.getEmailId());
	}
	
	public void markErrorNotificationProcessed(ErrorNotificationRecord record) {
		template.update(notificationProcessedQuery, record.getMessageProcessedFlag(), record.getMessageProcessedBy(), record.getMessageNotificationId());
	}
	
}
