package com.swiss.wallet.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.swiss.wallet.entity.TokenNotification;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.repository.ITokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final ITokenRepository iTokenRepository;

    public NotificationService(ITokenRepository iTokenRepository) {
        this.iTokenRepository = iTokenRepository;
    }


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

    public List<TokenNotification> findAllByUser(UserEntity user) {

        return iTokenRepository.findAllByUser(user);

    }
}
