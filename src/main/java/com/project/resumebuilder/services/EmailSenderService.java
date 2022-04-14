package com.project.resumebuilder.services;

import com.project.resumebuilder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String subject, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        message.setFrom("pranavmisgay@gmail.com");


        javaMailSender.send(message);
        System.out.println("Mail Sent....");
    }

    public char[] generateOTP(int length) {
        String numbers = "1234567890";
        Random random = new Random();
        char[] otp = new char[length];
        for(int i = 0; i< length ; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return otp;
    }


    public void sendVerificationEmail(User user, String siteURL)
    {
        SimpleMailMessage message = new SimpleMailMessage();

        String subject = "Verify your email";
        String body = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>";
        body = body.replace("[[name]]", user.getUserName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerification_code();
        body = body.replace("[[URL]]", verifyURL);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("pranavmisgay@gmail.com");
        javaMailSender.send(message);
    }




}
