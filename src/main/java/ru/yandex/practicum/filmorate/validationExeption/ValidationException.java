package ru.yandex.practicum.filmorate.validationExeption;

public class ValidationException extends Throwable {
    public ValidationException(String message) {
        super(message);
    }
}
