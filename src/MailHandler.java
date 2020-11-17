import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Utility class for sending mails
 * References the given SendMailTLS class
 *
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.7
 * @since 1.6
 */
public class MailHandler {
		/**
		 * Used to send an email.
		 * By default, email is sent from a dummy account created by us.
		 * If you wish to use your own email, edit mailCredentials.txt in the data folder
		 * @param recipient Recipient email address
		 * @param messageText Message to be sent in the email
		 * @param subject Subject of the email
		 * @return true if email was successfully sent, false if not
		 */
		public static boolean sendMail(String recipient, String messageText, String subject) {
		String credentials = readCredentials();
		String username = credentials.split("[:\n]")[1].trim();
		String password = credentials.split("[:\n]")[3].trim();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setText(messageText);
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	/**
	 * Retrieves credentials for email account used to send emails from the system
	 * Credentials can be edited in the mailCredentials.txt file in the data folder
	 * @return String containing the credentials to be used. Must follow the given format in the txt file
	 * Username:example@gmail.com
	 * Password:example_password
	 */
	private static String readCredentials() {
		try {
			File file = new File("data/mailCredentials.txt");// Input credentials in mailCredentials.txt in data folder
			Scanner sc = new Scanner(file);
			sc.useDelimiter("\\Z");
			return sc.next();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}