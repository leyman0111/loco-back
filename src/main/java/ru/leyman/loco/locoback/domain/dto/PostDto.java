package ru.leyman.loco.locoback.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO поста")
public record PostDto(Long id, Author author, LocalDateTime created,
                      String text, List<Long> contents,
                      Float latitude, Float longitude) {
}
