package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.RealiseDateConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Film {
    private long id;
    @Size(max = 200)
    private String description;
    @NotBlank
    private String name;
    @RealiseDateConstraint
    private LocalDate releaseDate;
    @Positive
    private Long duration;
    private Genre genre;
    private MPA mpa;
}
