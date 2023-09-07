package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    void deleteUser(@PathVariable int id);

    User createUser(@RequestBody User user);

    User updateUser(@RequestBody User userToUpdate);

    User findUser(@PathVariable int id);

}