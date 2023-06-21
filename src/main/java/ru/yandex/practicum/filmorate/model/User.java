package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    @Email
    @NotEmpty
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
