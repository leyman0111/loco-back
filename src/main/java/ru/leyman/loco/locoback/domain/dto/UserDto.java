package ru.leyman.loco.locoback.domain.dto;

import ru.leyman.loco.locoback.domain.enums.Locale;

public record UserDto(Long id, String username, String name,
                      String email, Locale locale, byte[] avatar) {
}
