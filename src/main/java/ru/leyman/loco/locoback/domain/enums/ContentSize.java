package ru.leyman.loco.locoback.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentSize {
    SMALL(64, 64), MEDIUM(256, 256), LARGE(512, 512);

    private final int length;
    private final int width;

}
