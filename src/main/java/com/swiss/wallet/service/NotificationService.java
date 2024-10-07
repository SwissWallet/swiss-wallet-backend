package com.swiss.wallet.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();

        String response;
        try {
            response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Mensagem enviada: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}
