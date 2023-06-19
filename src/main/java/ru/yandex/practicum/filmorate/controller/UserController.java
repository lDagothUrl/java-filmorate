package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Validated
public class UserController {
    private static final Map<Long, User> users = new HashMap<>();

    private long generatorId = 0;

    public long generateId() {
        return ++generatorId;
    }

    @GetMapping("/users")
    public Object[] getUsers() {
        log.debug("getUsers");
        return users.values().toArray();
    }

    @PostMapping("/users")
    public User postUsers(@RequestBody @Valid User user) {
        log.info("postUser-start: {}", user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User putUsers(@RequestBody @Valid User user) {
        log.info("postUser-start: {}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("UserId: " + user.getId());
        }
    }
}
