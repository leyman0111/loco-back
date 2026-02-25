package ru.leyman.loco.locoback.domain.dto;

import java.math.BigDecimal;

/**
 * DTO для карты
 */
public record PostMark(Long id, BigDecimal latitude, BigDecimal longitude) {
}
