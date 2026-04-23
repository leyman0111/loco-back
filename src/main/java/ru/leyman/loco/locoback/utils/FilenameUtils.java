package ru.leyman.loco.locoback.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FilenameUtils {

    public static String replaceFilename(String originFilenameWithExtension,
                                         String newFilename) {
        var chunked = originFilenameWithExtension.split("\\.");
        var extension = chunked[chunked.length - 1];
        return String.format("%s.%s", newFilename, extension);
    }

}
