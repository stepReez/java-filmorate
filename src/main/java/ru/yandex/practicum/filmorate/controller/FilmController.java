package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private static int idCounter = 1;
    List<Film> films = new ArrayList<>();

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        if(film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if(film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895");
        }
        if(film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
        film.setId(idCounter);
        films.add(film);
        idCounter++;
        log.info("Фильм создан успешно");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if(film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if(film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895");
        }
        if(film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
        if (films.size() >= film.getId()) {
            films.set(film.getId() - 1, film);
            log.info("Фильм обновлен успешно");
            return film;
        } else {
            throw new ValidationException("Фильма с таким id не существует");
        }
    }
}
