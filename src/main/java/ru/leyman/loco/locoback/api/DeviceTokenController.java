package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.service.DeviceTokenService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Устройства", description = "Регистрация FCM-токенов для пуш-уведомлений")
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    @Operation(summary = "Зарегистрировать FCM-токен устройства")
    @PostMapping("/device-token")
    public ResponseEntity<Void> registerDeviceToken(@RequestBody DeviceTokenRequest request) {
        deviceTokenService.registerToken(request.token());
        return ResponseEntity.ok().build();
    }

    public record DeviceTokenRequest(String token) {}
}