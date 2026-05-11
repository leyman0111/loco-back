package ru.leyman.loco.locoback.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.leyman.loco.locoback.domain.entity.DeviceToken;
import ru.leyman.loco.locoback.domain.entity.User;
import ru.leyman.loco.locoback.domain.repo.DeviceTokenRepo;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final DeviceTokenRepo deviceTokenRepo;

    /**
     * Send a push notification to all devices of the given user.
     */
    public void sendToUser(User user, String title, String body) {
        List<DeviceToken> tokens = deviceTokenRepo.findAllByUser(user);
        if (tokens.isEmpty()) {
            log.debug("[FCM] No device tokens for user {}", user.getId());
            return;
        }
        for (DeviceToken deviceToken : tokens) {
            try {
                Message message = Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .setToken(deviceToken.getToken())
                        .build();
                String response = FirebaseMessaging.getInstance().send(message);
                log.info("[FCM] Sent to user {} (token ...{}): {}",
                        user.getId(),
                        deviceToken.getToken().substring(
                                Math.max(0, deviceToken.getToken().length() - 8)),
                        response);
            } catch (Exception e) {
                log.warn("[FCM] Failed to send to token ...{}: {}",
                        deviceToken.getToken().substring(
                                Math.max(0, deviceToken.getToken().length() - 8)),
                        e.getMessage());
                // Optionally remove invalid tokens here
            }
        }
    }
}