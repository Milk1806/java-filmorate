package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @Min(1)
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    private Set<Genre> genre = new HashSet<>();
    private AgeRating ageRating;
}
