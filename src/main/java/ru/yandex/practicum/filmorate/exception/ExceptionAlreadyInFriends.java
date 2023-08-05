package ru.yandex.practicum.filmorate.exception;

public class ExceptionAlreadyInFriends extends RuntimeException {
    public ExceptionAlreadyInFriends(String s) {
        super("already in friends" + s);
    }
}
