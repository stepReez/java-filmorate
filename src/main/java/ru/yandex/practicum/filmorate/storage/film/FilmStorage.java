package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film findFilm(int id);

    void addLike(int filmId, int user);

    void removeLike(int filmId, int user);

    List<Film> getTop(String count);

    Mpa findMpaById(int id);

    List<Mpa> findAllMpa();

    Genre findGenreById(int id);

    List<Genre> findAllGenres();
}
