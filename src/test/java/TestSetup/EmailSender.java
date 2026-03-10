package TestSetup;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;



public class EmailSender {

    public static void main(String[] args) {

        final String senderEmail = "automationtest099@gmail.com";
        final String appPassword = "opkhnlgebmctvmih";
        final String recipientEmail = "automationtest099@gmail.com";

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.port", "587");

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        session.setDebug(true);
        
        try {
            // Create Email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Test Email From QA Automation");
            message.setText("Hello \n This is a test email from Java \n Regards, \nQA Team");

            // Send Email
            Transport.send(message);
            System.out.println("Email Sent Successfully");

        } catch (Exception e) {
        	e.printStackTrace();
    }}
}

