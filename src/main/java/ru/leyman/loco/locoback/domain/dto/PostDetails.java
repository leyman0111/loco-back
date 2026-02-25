package ru.leyman.loco.locoback.domain.dto;


import java.time.LocalDateTime;
import java.util.List;

public record PostDetails(LocalDateTime created, Author author,
                          String text, List<Long> contents,
                          List<ReactionDto> reactions,
                          List<CommentDto> comments) {
}
