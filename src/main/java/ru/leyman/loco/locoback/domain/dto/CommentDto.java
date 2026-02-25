package ru.leyman.loco.locoback.domain.dto;

import java.util.List;

public record CommentDto(Long id, Long postId, Author author, String text, List<ReactionDto> reactions) {
}
