package ru.yandex.practicum.filmorate.validationExeption;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
