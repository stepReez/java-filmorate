package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Optional<List<Film>> findAllFilms(String count);

    Optional<Film> findFilmById(int id);

    Optional<List<Genre>> findAllGenres(int id);

    Optional<Integer> findFilmLikeCount(int id);

    Optional<Film> createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<List<Film>> getTop(String count);

    void addLike(int filmId, int user);

    void removeLike(int filmId, int user);

    Optional<Mpa> findMpaById(int id);

    Optional<List<Mpa>> findAllMpa();

    Optional<Genre> findGenreById(int id);

    Optional<List<Genre>> findAllGenres();
}
