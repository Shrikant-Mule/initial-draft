package com.copart.emailsender.domain;

import java.util.Map;

public class EmailRecord {
	
	private Long templateId;
	private String templateName;
	private Long emailId;
	private String to;
	private String preferredLanguage;
	private String processedFlag;
	private String processedBy;
	private String message;
	private Map<String, Object> parameters;
	private String emailBody;
	private String companyCode;
	private String country;
	private String alternateTo;
	
	public String getProcessedFlag() {
		return processedFlag;
	}
	public void setProcessedFlag(String processedFlag) {
		this.processedFlag = processedFlag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	private String subject;
	
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public Long getEmailId() {
		return emailId;
	}
	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getPreferredLanguage() {
		return preferredLanguage;
	}
	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}
	@Override
	public String toString() {
		return "EmailRecord [templateId=" + templateId + ", templateName="
				+ templateName + ", emailId=" + emailId + ", to=" + to
				+ ", preferredLanguage=" + preferredLanguage
				+ ", processedFlag=" + processedFlag + ", message=" + message
				+ ", parameters=" + parameters + ", subject=" + subject + "]";
	}
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	public String getProcessedBy() {
		return processedBy;
	}
	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAlternateTo() {
		return alternateTo;
	}
	public void setAlternateTo(String alternateTo) {
		this.alternateTo = alternateTo;
	}
	
	
	
}
