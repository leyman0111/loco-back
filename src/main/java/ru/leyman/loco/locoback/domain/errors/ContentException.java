package ru.leyman.loco.locoback.domain.errors;

/**
 * Ошибка при работкее с контентом
 */
public class ContentException extends RuntimeException {

    public ContentException(String message) {
        super(message);
    }

}
