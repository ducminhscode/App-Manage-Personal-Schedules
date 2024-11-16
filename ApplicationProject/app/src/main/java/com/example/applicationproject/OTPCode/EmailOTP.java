package com.example.applicationproject.OTPCode;

import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.AuthenticationFailedException;
import javax.mail.SendFailedException;


public class EmailOTP extends AsyncTask<Void, Void, Boolean> {
    private String email;
    private String subject;
    private String message;

    public EmailOTP(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("2251012095minh@ou.edu.vn", "212890366");
                }
            });

            javax.mail.Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("2251012095minh@ou.edu.vn"));
            msg.setRecipients(javax.mail.Message.RecipientType.TO,InternetAddress.parse(email));
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }
    }
}


