package ru.leyman.loco.locoback.domain.dto;

import ru.leyman.loco.locoback.domain.enums.PostCategory;

/**
 * DTO для карты
 */
public record PostMark(Long id, Float latitude, Float longitude, PostCategory category) {
}
