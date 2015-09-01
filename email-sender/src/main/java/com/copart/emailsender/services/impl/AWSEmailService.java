package com.copart.emailsender.services.impl;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.copart.emailsender.domain.EmailRecord;

@Component
public class AWSEmailService extends EmailServiceImpl {

	@Autowired
	private AmazonSimpleEmailServiceClient awsEmailClient;
	

	@Override
	public void sendEmail(final EmailRecord record) throws Exception {
        Destination destination = new Destination();
        
        //The alternate Email will be set only in DEV and QA Env
        //This is to make sure that we are not sending any mails to users from DEV and QA env
        if (StringUtils.isNotBlank(record.getAlternateTo())) {
            destination.withToAddresses(record.getAlternateTo());
            logger.debug("Sending " + record.getTemplateName() + " to Alternate : " + record.getAlternateTo());
        }
        else {
        	destination.withToAddresses(record.getTo());
        	logger.debug("Sending " + record.getTemplateName() + " to: " + record.getTo());
        }
        
        // Create the subject and body of the message.
        Content subject = new Content().withData(record.getSubject());
        Content textBody = new Content().withData(record.getEmailBody());
        Body body = new Body().withHtml(textBody);
        
        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);
        
        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(fromEmail).withDestination(destination).withMessage(message);
        awsEmailClient.sendEmail(request);
	}

}
