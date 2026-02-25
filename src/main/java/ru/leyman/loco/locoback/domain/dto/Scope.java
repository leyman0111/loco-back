package ru.leyman.loco.locoback.domain.dto;

import ru.leyman.loco.locoback.domain.enums.PostCategory;

import java.math.BigDecimal;
import java.util.List;

public record Scope(BigDecimal latitude, BigDecimal longitude,
                    Integer distance, List<PostCategory> categories) {

}
