package com.copart.emailsender.domain;

import java.util.Map;


public class ErrorNotificationRecord extends EmailRecord {

	private Long messageNotificationId;
	private String notificationDesc;
	/*private String processedFlag;*/
	private String processedMessage;
	private String sourceTableName;
	private String sourceIdColumnName;
	private Long sourceIdValue;
	private String sourceDataList;
	private String messageProcessedBy;
	private String messageProcessedFlag;
	/*private String companyCode;*/
	/*private Map<String, Object> parameters;*/
	/*private String templateName;
	private String to;
	private String preferredLanguage;
	private String emailBody;
	private String subject;*/
	
	public Long getMessageNotificationId() {
		return messageNotificationId;
	}
	public void setMessageNotificationId(Long messageNotificationId) {
		this.messageNotificationId = messageNotificationId;
	}
	public String getNotificationDesc() {
		return notificationDesc;
	}
	public void setNotificationDesc(String notificationDesc) {
		this.notificationDesc = notificationDesc;
	}
	public String getProcessedMessage() {
		return processedMessage;
	}
	public void setProcessedMessage(String processedMessage) {
		this.processedMessage = processedMessage;
	}
	public String getSourceTableName() {
		return sourceTableName;
	}
	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}
	public String getSourceIdColumnName() {
		return sourceIdColumnName;
	}
	public void setSourceIdColumnName(String sourceIdColumnName) {
		this.sourceIdColumnName = sourceIdColumnName;
	}
	public Long getSourceIdValue() {
		return sourceIdValue;
	}
	public void setSourceIdValue(Long sourceIdValue) {
		this.sourceIdValue = sourceIdValue;
	}
	public String getSourceDataList() {
		return sourceDataList;
	}
	public void setSourceDataList(String sourceDataList) {
		this.sourceDataList = sourceDataList;
	}
	public String getMessageProcessedBy() {
		return messageProcessedBy;
	}
	public void setMessageProcessedBy(String messageProcessedBy) {
		this.messageProcessedBy = messageProcessedBy;
	}
	public String getMessageProcessedFlag() {
		return messageProcessedFlag;
	}
	public void setMessageProcessedFlag(String messageProcessedFlag) {
		this.messageProcessedFlag = messageProcessedFlag;
	}
	
	
}
