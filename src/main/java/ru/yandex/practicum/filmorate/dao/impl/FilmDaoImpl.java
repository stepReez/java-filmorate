package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Component
public class FilmDaoImpl implements FilmDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<Film>> findAllFilms() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT \"id\" FROM \"users\" LIMIT 100");
        while (filmRows.next()) {
            Optional<Film> filmOptional = findFilmById(filmRows.getInt("id"));
            if (filmOptional.isPresent()) {
                Film film = filmOptional.get();
                films.add(film);
            }
        }
        return Optional.of(films);
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT *" +
                        " FROM \"films\" " +
                "JOIN \"rating\" ON \"rating\" = \"rating_id\" " +
                "WHERE \"id\" = ?", id);
        if (filmRows.next()) {
            log.info("Найден фильм: {}", filmRows.getInt("id"));
            Film film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releaseDate").toLocalDate(),
                    filmRows.getInt("duration"),
                    findMpaById(filmRows.getInt("rating")).get(),
                    findAllGenres(id).get(),
                    findFilmLikeCount(id).get()
            );
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    public Optional<List<Genre>> findAllGenres(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM \"genre_film\" " +
                        "JOIN \"genres\" ON \"genre_id\" = \"id\" " +
                        "WHERE \"film_id\" = ?", id);
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            genres.add(findGenreById(genreRows.getInt("id")).get());
        }
        return Optional.of(genres);
    }

    public Optional<Integer> findFilmLikeCount(int id) {
        SqlRowSet likeCount = jdbcTemplate.queryForRowSet(
                "SELECT COUNT(\"user_id\") as likes " +
                        "FROM \"likes_films\" " +
                        "WHERE \"film_id\" = ?", id
        );
        if (likeCount.next()) {
            log.info("Найдено количество лайков фильма {}", id);
            return Optional.of(likeCount.getInt("likes"));
        } else {
            log.info("У фильма с идентификатором {} лайки не найдены.", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Film> createFilm(Film film) {
        String sqlQuery = "insert into \"films\" (\"id\", \"name\"," +
                " \"description\", \"releaseDate\", \"duration\", \"rating\") " +
                "values (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
                );

        List<Genre> genre = film.getGenres();
        if (genre != null) {
            for (Genre genreId : genre) {
                jdbcTemplate.update("INSERT INTO \"genre_film\" (\"film_id\", \"genre_id\") " +
                        "VALUES (?, ?)", film.getId(), genreId.getId());
            }
        }

        return findFilmById(film.getId());
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        jdbcTemplate.update("DELETE FROM \"genre_film\" WHERE \"film_id\" = ?", film.getId());
        String sqlQuery = "update \"films\" set " +
                "\"name\" = ?, \"description\" = ?, \"releaseDate\" = ?, \"duration\" = ?, \"rating\" = ? " +
                "where \"id\" = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        System.out.println(film);
        System.out.println(film.getMpa());
        List<Genre> genre = film.getGenres();
        if (genre != null) {
            for (Genre genreId : genre) {
                jdbcTemplate.update("MERGE INTO \"genre_film\" " +
                        "(\"film_id\", \"genre_id\") " +
                        "KEY(\"film_id\", \"genre_id\") " +
                        "VALUES (?, ?)", film.getId(), genreId.getId());
            }
        }
        System.out.println(findFilmById(film.getId()).get());
        return findFilmById(film.getId());
    }

    @Override
    public Optional<List<Film>> getTop(String count) {
        List<Film> top = new ArrayList<>();

        SqlRowSet topRow = jdbcTemplate.queryForRowSet(
                "SELECT \"id\" " +
                        "FROM \"films\" " +
                        "LEFT JOIN \"likes_films\" ON \"id\" = \"film_id\" " +
                        "GROUP BY \"id\" " +
                        "ORDER BY COUNT(\"user_id\") DESC " +
                        "LIMIT ?", count
        );

        while (topRow.next()) {
            top.add(findFilmById(topRow.getInt("id")).get());
        }
        return Optional.of(top);
    }

    @Override
    public void addLike(int filmId, int user) {
        jdbcTemplate.update("INSERT INTO \"likes_films\" (\"film_id\", \"user_id\") VALUES (?, ?)", filmId, user);
    }

    @Override
    public void removeLike(int filmId, int user) {
        jdbcTemplate.update("DELETE FROM \"likes_films\" " +
                "WHERE \"film_id\" = ? AND \"user_id\" = ?", filmId, user);
    }

    @Override
    public Optional<Mpa> findMpaById(int id) {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(
                "SELECT * FROM \"rating\" WHERE \"rating_id\" = ?", id
        );

        if (mpaRow.next()) {
            Mpa mpa = new Mpa(id, mpaRow.getString("rating_name"));
            return Optional.of(mpa);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Mpa>> findAllMpa() {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT * FROM \"rating\" ORDER BY \"rating_id\"");
        List<Mpa> mpaSet = new ArrayList<>();

        while (mpaRow.next()) {
            mpaSet.add(new Mpa(mpaRow.getInt("rating_id"), mpaRow.getString("rating_name")));
        }

        return Optional.of(mpaSet);
    }

    @Override
    public Optional<Genre> findGenreById(int id) {
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(
                "SELECT * FROM \"genres\" WHERE \"id\" = ?", id
        );

        if (genreRow.next()) {
            return Optional.of(new Genre(id, genreRow.getString("name")));
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Genre>> findAllGenres() {
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM \"genres\" ORDER BY \"id\"");
        List<Genre> genreSet = new ArrayList<>();

        while (genreRow.next()) {
            genreSet.add(new Genre(genreRow.getInt("id"), genreRow.getString("name")));
        }
        return Optional.of(genreSet);
    }
}
