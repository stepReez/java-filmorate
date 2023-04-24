package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final FilmDao filmDao;

    private static int idCounter = 1;

    public FilmDbStorage(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Override
    public List<Film> getFilms() {
        return filmDao.findAllFilms().get();
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        film.setId(idCounter);
        idCounter++;
        return filmDao.createFilm(film).get();
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        if (film.getId() > idCounter) {
            throw new NotFoundException(String.format("Фильма с id %d не существует", film.getId()));
        }
        return filmDao.updateFilm(film).get();
    }

    @Override
    public Film findFilm(int id) {
        try {
            return filmDao.findFilmById(id).get();
        } catch (NoSuchElementException e) {
            throw new NotFoundException(String.format("Фильм с id=%d не найден", id));
        }
    }

    @Override
    public void addLike(int filmId, int user) {
        filmDao.addLike(filmId, user);
    }

    @Override
    public void removeLike(int filmId, int user) {
        filmDao.removeLike(filmId, user);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Неверная дата релиза");
        }
    }

    @Override
    public List<Film> getTop(String count) {
        return filmDao.getTop(count).get();
    }

    @Override
    public Mpa findMpaById(int id) {
        if (filmDao.findFilmById(id).isPresent()) {
            return filmDao.findMpaById(id).get();
        } else {
            throw new NotFoundException(String.format("Рейтинга с id %d не существует", id));
        }

    }

    @Override
    public List<Mpa> findAllMpa() {
        return filmDao.findAllMpa().get();
    }

    @Override
    public Genre findGenreById(int id) {
        if (filmDao.findGenreById(id).isPresent()) {
            return filmDao.findGenreById(id).get();
        } else {
            throw new NotFoundException(String.format("Жанра с id %d не существует", id));
        }
    }

    @Override
    public List<Genre> findAllGenres() {
        return filmDao.findAllGenres().get();
    }
}
