package com.copart.emailsender.services;

import com.copart.emailsender.domain.EmailRecord;
import com.copart.emailsender.domain.ErrorNotificationRecord;

public interface EmailService {

	
	public void processEmail(EmailRecord record);
	
	public void processEmailNotification(ErrorNotificationRecord record);
}
