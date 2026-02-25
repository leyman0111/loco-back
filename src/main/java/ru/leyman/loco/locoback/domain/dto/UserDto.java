package ru.leyman.loco.locoback.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.leyman.loco.locoback.domain.enums.Locale;

@Schema(description = "DTO пользователя")
public record UserDto(Long id, String username, String name,
                      String email, Locale locale, byte[] avatar) {
}
