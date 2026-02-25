package ru.leyman.loco.locoback.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FilenameUtil {

    public static String getExtension(String filename) {
        var chunked = filename.split("\\.");
        return chunked[chunked.length - 1];
    }

}
