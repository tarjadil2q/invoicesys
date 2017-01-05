package com.pce.service;

import com.pce.exception.InvalidMailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by Leonardo Tarjadi on 3/01/2017.
 */
@Service
public class EmailServiceImpl implements EmailService {


  private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  private static final String footerEmail = "<br/><br/> Best Regards, <br/> PCE System<br/><br/>" +
          "----This is system generated message. Please do not reply to this email----";
  public static final String MAIL_SMTP_HOST = "mail.smtp.host";
  public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
  public static final String MAIL_SMTP_PORT = "mail.smtp.port";
  public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";


  @Value("${pce.from.email}")
  private String fromEmail;
  @Value("${pce.email.password}")
  private String emailPassword;


  @Value("${" + MAIL_SMTP_HOST + "}")
  private String smtpHost;
  @Value("${" + MAIL_SMTP_AUTH + "}")
  private boolean smtpAuth;
  @Value("${" + MAIL_SMTP_PORT + "}")
  private int smtpPort;
  @Value("${" + MAIL_SMTP_STARTTLS_ENABLE + "}")
  private boolean smtpStartTls;


  private Properties mailProperties;


  private Session session;


  private MimeMessage createMimeMessage(String to, String subject, String bodyText) throws MessagingException {

    MimeMessage email = new MimeMessage(getMailSession());

    email.setFrom(new InternetAddress(fromEmail));
    email.addRecipient(javax.mail.Message.RecipientType.TO,
            new InternetAddress(to));
    email.setSubject(subject);
    email.setContent("<p>" + bodyText + footerEmail + "</p>", "text/html");

    return email;
  }


  @Override
  @Async
  public Future<Boolean> sendEmail(String to, String subject, String bodyText) {
    try {
      MimeMessage mimeMessage = createMimeMessage(to, subject, bodyText);
      logger.debug("Sending email with message " + bodyText);
      Transport.send(mimeMessage);
      return new AsyncResult<>(Boolean.TRUE);

    } catch (MessagingException e) {
      logger.error("Exception when sending email ", e);
      throw new InvalidMailException("Exception when sending email " + e.getMessage(), e);
    }

  }


  private Session getMailSession() {
    if (session == null && mailProperties == null) {
      mailProperties = new Properties();
      mailProperties.put(MAIL_SMTP_HOST, smtpHost);
      mailProperties.put(MAIL_SMTP_AUTH, smtpAuth);
      mailProperties.put(MAIL_SMTP_PORT, smtpPort);
      mailProperties.put(MAIL_SMTP_STARTTLS_ENABLE, smtpStartTls);
      session = Session.getInstance(mailProperties,
              new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(fromEmail, emailPassword);
                }
              });

      return session;

    }
    return session;
  }

}
