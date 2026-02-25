package ru.leyman.loco.locoback.domain.dto;

import ru.leyman.loco.locoback.domain.enums.PostCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PostDto(Long id, Author author, LocalDateTime created,
                      String text, PostCategory category,
                      BigDecimal latitude, BigDecimal longitude) {
}
