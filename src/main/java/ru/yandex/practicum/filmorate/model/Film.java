package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private int id;
    @NotEmpty(message = "empty")
    private String name;
    @Size(max = 200, message = "length more 200")
    private String description;
    @NotNull(message = "null")
    @PastOrPresent(message = "after 1895.12.28")
    private LocalDate releaseDate;
    @Positive(message = "negative")
    private int duration;
    private Set<Integer> likes;
    @NotEmpty(message = "empty")
    @Size(min = 1, message = "size < 1")
    private Set<Genre> genres;
    private Mpa mpa;


    public Film(String name, String description, LocalDate releaseDate, int duration, int id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genres = new HashSet<>();
    }


}