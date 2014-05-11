/**
 * Code inspired by http://stackoverflow.com/questions/46663/how-to-send-an-email-by-java-application-using-gmail-yahoo-hotmail#47452
 */

package email;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.tomcat.jni.Time;

public class EmailSender {

	private String USER_NAME = "parcelsonthemove";  // GMail user name (just the part before "@gmail.com")
	private String PASSWORD = "rogervenerable"; // GMail password

	public EmailSender(String parcelContent, String parcelDescription, String previousStatus, String newStatus, String senderEmail,	String receiverEmail){
		String subject = "PotM: parcel status update.";
		String from = USER_NAME;
		String pass = PASSWORD;
		String body = "Dear Customer, \n The status of your parcel (Name: " + parcelContent + "; Description: " + parcelDescription + ") has just changed from " + previousStatus + " to " + newStatus + ". \n Best regards, \n The PotM Team.";
		String[] to = { senderEmail, receiverEmail };
		
		System.out.println("email 1 :" + senderEmail);
		System.out.println("email 2 :" + receiverEmail);
		
		sendFromGMail(from, pass, to, subject, body);
	}

	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for( int i = 0; i < to.length; i++ ) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for( int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtps");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		}
		catch (AddressException ae) {
			ae.printStackTrace();
		}
		catch (MessagingException me) {
			me.printStackTrace();
		}

		System.out.println("Email sent");
	}
}