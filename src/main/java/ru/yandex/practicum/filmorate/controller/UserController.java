package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private static final Map<Long, User> users = new HashMap<>();

    private long generatorId = 0;

    private long generateId() {
        return ++generatorId;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.debug("getUsers");
        return new ArrayList<>(users.values());
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
