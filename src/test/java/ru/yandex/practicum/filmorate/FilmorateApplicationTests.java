package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDaoImpl userDao;

	private final UserDbStorage userStorage;

	private final FilmDaoImpl filmDao;

	@Test
	public void testFindUserById() {
		userDao.createUser(new User(1, "qwe@gmail.com", "fc", "Tyler", LocalDate.EPOCH));
		Optional<User> userOptional = userDao.findUserById(1);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testCreateUser() {

		Optional<User> userOptional =
				userDao.createUser(new User(2, "qwe@gmail.com", "cf", "Durden", LocalDate.EPOCH));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("login", "cf")
				);
	}

	@Test
	public void testFindAllConfirmedFriends() {

		userDao.createUser(new User(4, "qwe@gmail.com", "cf", "Durden", LocalDate.EPOCH));
		userDao.createUser(new User(5, "qwe@gmail.com", "fc", "Tyler", LocalDate.EPOCH));
		userStorage.addFriend(4, 5);
		userStorage.addFriend(5, 4);

		Optional<Set<Integer>> setOptional = userDao.findAllConfirmedFriends(4);
		assertThat(setOptional)
				.isPresent()
				.hasValueSatisfying(set ->
						assertThat(set).hasSize(1)
				);
	}

	@Test
	public void testCreateFilm() {
		Optional<Film> setOptional =
				filmDao.createFilm(new Film(1, "123", "312", LocalDate.now(),
						100, new Mpa(1), new ArrayList<>(), 0));

		assertThat(setOptional)
				.isPresent()
				.hasValueSatisfying(list ->
						assertThat(list).hasFieldOrPropertyWithValue("name", "123")
				);
	}

	@Test
	public void testFindAllGenres() {

		Optional<List<Genre>> setOptional = filmDao.findAllGenres();
		assertThat(setOptional)
				.isPresent()
				.hasValueSatisfying(list ->
						assertThat(list).hasSize(6)
				);
	}

	@Test
	public void testFindAllMpa() {

		Optional<List<Mpa>> setOptional = filmDao.findAllMpa();
		assertThat(setOptional)
				.isPresent()
				.hasValueSatisfying(list ->
						assertThat(list).hasSize(5)
				);
	}

	@Test
	public void testFindFilmById() {
		filmDao.createFilm(new Film(2, "123", "312", LocalDate.now(),
				100, new Mpa(1), new ArrayList<>(), 0));

		Optional<Film> userOptional = filmDao.findFilmById(2);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 2)
				);
	}
}