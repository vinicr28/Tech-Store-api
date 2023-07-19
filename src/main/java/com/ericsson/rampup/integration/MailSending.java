package com.ericsson.rampup.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailSending {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${support.mail}")
    private String mailFrom;

    public void sendMail(String mailTo, String password) throws MessagingException{
        MimeMessage mail= javaMailSender.createMimeMessage();

        MimeMessageHelper mailMessage = new MimeMessageHelper(mail);

        mailMessage.setSubject("User created!");
        mailMessage.setText("Hello!\n" +
                "\n" +
                "Welcome to our website! We are thrilled to have you with us and provide access to cutting-edge technology solutions to help you achieve your goals.\n" +
                "\n" +
                "You can log in using your email address and password:\n" +
                "\n" +
                "Email: " + mailTo + "\n" +
                "Password: " + password + "\n" +
                "\n" +
                "If you have any questions or need assistance, please do not hesitate to contact our dedicated support team.\n" +
                "\n" +
                "Thank you for choosing our company. We look forward to helping you explore all the possibilities that technology has to offer.\n" +
                "\n" +
                "Best regards,\n" +
                "Vincius Camargo Reis\n" +
                "Minimal Tech Store");
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(mailTo);
        javaMailSender.send(mail);
    }
}
