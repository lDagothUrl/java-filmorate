package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static int nextId = 0;


    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public void deleteUser(int id) {
        if (users.remove(id) == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
    }

    public User createUser(User user) {
        validate(user);
        int id = ++nextId;
        user.setId(id);
        users.put(user.getId(), user);
        return user;


    }

    public User updateUser(User userToUpdate) {
        User existingUser = users.get(userToUpdate.getId());
        if (existingUser == null) {
            throw new InternalServerException("User with id " + userToUpdate.getId() + " not found");
        }
        validate(userToUpdate);

        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setBirthday(userToUpdate.getBirthday());
        existingUser.setLogin(userToUpdate.getLogin());
        existingUser.setName(userToUpdate.getName());

        return existingUser;
    }

    public User findUser(int id) {
        if (users.get(id) != null) {
            return users.get(id);
        } else {
            throw new NotFoundException("user with id:" + id + " not found");
        }
    }


    public void validate(User user) {
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();

        if (email.trim().isEmpty() || !email.contains("@")) {
            throw new ValidationException("Email must contain @ symbol");
        }
        if (login.trim().isEmpty() || login.contains(" ")) {
            throw new ValidationException("Login must not contain spaces");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }
    }
}