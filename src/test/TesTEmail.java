package test;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class TesTEmail {
    public static void sendEmail(String to, String subject, String body) {
        // Sender's email credentials
        final String username = "jitheshjalapothu@gmail.com"; // replace with your email
        final String password = "bilk kdtp rbye wswl";       // replace with your email password or app password

        // Set mail properties
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
