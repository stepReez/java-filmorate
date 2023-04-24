package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Film {
    private int id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @Past
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Mpa mpa;

    private List<Genre> genres;

    private int likes;

    public Film(int id, String name, String description,
                LocalDate releaseDate, int duration, Mpa mpa,
                List<Genre> genre, int likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genre;
        this.likes = likes;
    }
}
