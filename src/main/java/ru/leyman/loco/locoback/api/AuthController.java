package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.domain.dto.AuthRequest;
import ru.leyman.loco.locoback.domain.enums.AuthServer;
import ru.leyman.loco.locoback.service.security.LocoAuthorizeService;
import ru.leyman.loco.locoback.service.security.OAuthService;

import java.util.Map;

@Slf4j
@Tag(name = "Авторизация", description = "Раздел авторизации")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final Map<AuthServer, OAuthService> authServices;
    private final LocoAuthorizeService locoAuthorizeService;

    @Operation(description = "Авторизация через OAuth2")
    @GetMapping("{authServer}")
    public String authorize(@PathVariable AuthServer authServer, @RequestParam Map<String, String> params) {
        log.info("Received authorize by authServer={}, params={}", authServer, params);
        return authServices.get(authServer).exchangeForAccessToken(params);
    }

    @Operation(description = "Базовая авторизация")
    @PostMapping
    public String authorize(@RequestBody AuthRequest authRequest) {
        log.info("Received authorize by authRequest={}", authRequest);
        return locoAuthorizeService.authorize(authRequest);
    }

}
