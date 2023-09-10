package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j
@Data
@NonNull
@ToString
public class User {

    private int id;
    @NotEmpty(message = "empty")
    @Email
    private String email;
    @NotEmpty(message = "empty")
    @NotNull(message = "null")
    private String login;
    private String name;
    @NotNull(message = "null")
    private LocalDate birthday;
    public Map<Integer, FriendshipStatus> friends;
    private Set<Integer> pendingFriends;


    public User(String email, String login, String name, LocalDate birthday, int id) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name == null || name.trim().isEmpty() ? login : name;
        this.birthday = birthday;
        this.friends = new HashMap<>();
        this.pendingFriends = new HashSet<>();
    }


}