package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    private static int idCounter = 1;

    private static List<Film> films = new ArrayList<>();

    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);


    public List<Film> getFilms() {
        return films;
    }

    public Film createFilm(Film film) {
        validate(film);
        film.setId(idCounter);
        films.add(film);
        idCounter++;
        log.info("Фильм создан успешно");
        return film;
    }

    public Film updateFilm(Film film) {
        validate(film);
        if (films.size() >= film.getId()) {
            films.set(film.getId() - 1, film);
            log.info("Фильм обновлен успешно");
            return film;
        } else {
            throw new NotFoundException("Фильма с таким id не существует");
        }
    }

    public Film findFilm(int id) {
        if (films.size() >= id) {
            return films.get(id - 1);
        } else {
            throw new NotFoundException("Фильма с таким id не существует");
        }
    }

    @Override
    public void addLike(int filmId, int user) {
        findFilm(filmId).setLikes(findFilm(filmId).getLikes() + 1);
        log.info("Юзер " + user + " поставил лайк фильму " + filmId);
    }

    @Override
    public void removeLike(int filmId, int user) {
        findFilm(filmId).setLikes(findFilm(filmId).getLikes() - 1);
        log.info("Юзер " + user + " убрал лайк с фильма " + filmId);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895");
        }
    }

    public List<Film> getTop(String count) {
        Comparator<Film> comparator = Comparator.comparingInt(Film::getLikes);
        List<Film> list = getFilms()
                .stream()
                .sorted(comparator.reversed())
                .limit(Integer.parseInt(count))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public Mpa findMpaById(int id) {
        return null;
    }

    @Override
    public List<Mpa> findAllMpa() {
        return null;
    }

    @Override
    public Genre findGenreById(int id) {
        return null;
    }

    @Override
    public List<Genre> findAllGenres() {
        return null;
    }
}
