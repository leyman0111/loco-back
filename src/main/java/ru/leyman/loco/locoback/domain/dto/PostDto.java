package ru.leyman.loco.locoback.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.leyman.loco.locoback.domain.enums.PostCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO поста")
public record PostDto(Long id, Author author, LocalDateTime created,
                      String text, PostCategory category,
                      BigDecimal latitude, BigDecimal longitude) {
}
