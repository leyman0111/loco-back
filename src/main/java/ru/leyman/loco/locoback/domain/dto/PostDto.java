package ru.leyman.loco.locoback.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.leyman.loco.locoback.domain.enums.PostCategory;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO поста")
public record PostDto(Long id, Author author, LocalDateTime created,
                      String text, PostCategory category,
                      List<Long> contents,
                      Float latitude, Float longitude) {
}
