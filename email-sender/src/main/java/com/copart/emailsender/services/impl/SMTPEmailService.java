package com.copart.emailsender.services.impl;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.copart.emailsender.domain.EmailRecord;

@Component
public class SMTPEmailService extends EmailServiceImpl {

	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void sendEmail(final EmailRecord record) throws Exception {
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
	    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
	    message.setSubject(record.getSubject());
	    message.setFrom(fromEmail);
	    
	    // Changing the to address to alternate email
	    // alternate email should be set only in Dev and QA env
	    if (StringUtils.isNotBlank(record.getAlternateTo())) {
	    	message.setTo(record.getAlternateTo());
	    	logger.debug("Sending " + record.getTemplateName() + " to Alternate : " + record.getAlternateTo());
	    }
	    else {
		    message.setTo(record.getTo());
		    logger.debug("Sending " + record.getTemplateName() + " to: " + record.getTo());
	    }
	    message.setText(record.getEmailBody(), true); // true = isHtml
	    mailSender.send(mimeMessage);
	}

}
