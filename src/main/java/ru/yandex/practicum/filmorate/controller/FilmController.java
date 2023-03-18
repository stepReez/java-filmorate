package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private static int idCounter = 1;
    private static List<Film> films = new ArrayList<>();
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(idCounter);
        films.add(film);
        idCounter++;
        log.info("Фильм создан успешно");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validate(film);
        if (films.size() >= film.getId()) {
            films.set(film.getId() - 1, film);
            log.info("Фильм обновлен успешно");
            return film;
        } else {
            throw new ValidationException("Фильма с таким id не существует");
        }
    }

    private void validate(Film film) {
        if(film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895");
        }
    }
}
