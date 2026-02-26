package ru.leyman.loco.locoback.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.leyman.loco.locoback.domain.enums.PostCategory;

import java.util.List;

@Schema(description = "Область видимости на карте, по которой нужно искать посты")
public record Scope(Float latitude, Float longitude,
                    Integer distance, List<PostCategory> categories) {

}
