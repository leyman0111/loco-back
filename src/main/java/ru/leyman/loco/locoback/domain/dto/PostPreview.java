package ru.leyman.loco.locoback.domain.dto;

import ru.leyman.loco.locoback.domain.enums.PostCategory;

import java.time.LocalDateTime;
import java.util.List;

public record PostPreview(Long id, LocalDateTime created, Author author,
                          String text, PostCategory category,
                          List<Long> contents, List<ReactionDto> reactions) {
}
