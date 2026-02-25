package ru.leyman.loco.locoback.domain.dto;

import ru.leyman.loco.locoback.domain.enums.ReactionType;

public record ReactionDto(Long id, Long postId, Long commentId, Author author, ReactionType type) {
}
