package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.repository.user.StatusFriend;

public class ExceptionBlockedUserFriend extends RuntimeException {
    public ExceptionBlockedUserFriend(String status) {
        super(status.toString());
    }
}
