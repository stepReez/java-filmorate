package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

public interface FilmStorage {
    public List<Film> getFilms();
    public Film createFilm(Film film);
    public Film updateFilm(Film film);
    public Film findFilm(int id);
}
