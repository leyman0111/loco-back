package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.leyman.loco.locoback.domain.entity.DeviceToken;
import ru.leyman.loco.locoback.domain.repo.DeviceTokenRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepo deviceTokenRepo;
    private final UserService userService;

    /**
     * Register or update an FCM device token for the current authenticated user.
     * If the token already exists (from any user), it is re-assigned to the current user.
     */
    public void registerToken(String token) {
        var user = userService.getCurrentUser();
        deviceTokenRepo.findByToken(token).ifPresentOrElse(
                existing -> {
                    // Re-assign to current user in case of device re-use
                    existing.setUser(user);
                    deviceTokenRepo.save(existing);
                    log.info("[FCM] Updated token for user {}", user.getId());
                },
                () -> {
                    var deviceToken = new DeviceToken();
                    deviceToken.setUser(user);
                    deviceToken.setToken(token);
                    deviceTokenRepo.save(deviceToken);
                    log.info("[FCM] Registered new token for user {}", user.getId());
                }
        );
    }
}