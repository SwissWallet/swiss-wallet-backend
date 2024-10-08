package com.swiss.wallet.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FirebaseInitializer implements CommandLineRunner {


    @Value("${firebase.private.key}")
    private String privateKey;

    public JSONObject createPrivate() {
        String base64String = privateKey;
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        String jsonString = new String(decodedBytes, StandardCharsets.UTF_8);

        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject.toString(2));
        return jsonObject;
    }

    @Override
    public void run(String... args) {
        try {
            JSONObject serviceAccount = createPrivate();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(serviceAccount.toString().getBytes(StandardCharsets.UTF_8))))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized");
        } catch (Exception e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
