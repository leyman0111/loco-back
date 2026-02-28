package ru.leyman.loco.locoback.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.leyman.loco.locoback.service.security.OAuthService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthService authService;

    @GetMapping("yandex")
    public String exchangeForAccessToken(@RequestParam String authCode) {
        return authService.exchangeForAccessToken(authCode);
    }

}
