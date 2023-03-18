package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private final String name;
    @Size(max=200)
    private final String description;
    @Past
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}
