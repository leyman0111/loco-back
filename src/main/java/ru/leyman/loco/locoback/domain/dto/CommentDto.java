package ru.leyman.loco.locoback.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO комментария")
public record CommentDto(Long id, Long postId, Author author, String text, List<ReactionDto> reactions) {
}
