package com.example.internshipapp.service;

import com.example.internshipapp.model.User;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Async
public class SendGridEmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;
    private static final Logger logger = LoggerFactory.getLogger(SendGridEmailService.class);

    public void sendMail(User savedUser) {

        Email from = new Email("test@example.com");
        String subject = "New user created";
        Email to = new Email("konrad.stach00@gmail.com");
        Content content = new Content("text/plain", String.format("New user %s %s saved in database", savedUser.getFirstName(), savedUser.getLastName()));
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
}
