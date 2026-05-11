package ru.leyman.loco.locoback.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${fcm.firebase-service-account-path}")
    private String firebaseServiceAccountPath;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                var serviceAccount = new FileInputStream(firebaseServiceAccountPath);
                var options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
                log.info("[Firebase] Admin SDK initialized");
            }
        } catch (IOException e) {
            log.error("[Firebase] Failed to initialize Admin SDK: {}", e.getMessage());
        }
    }
}