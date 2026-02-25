package ru.leyman.loco.locoback.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.leyman.loco.locoback.domain.enums.ReactionType;

@Schema(description = "DTO реакции")
public record ReactionDto(Long id, Long postId, Long commentId, Author author, ReactionType type) {
}
