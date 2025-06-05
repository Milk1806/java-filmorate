package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController();
    private final Map<Long, Film> films = filmController.getFilms();
    private Film film;
    private Film newFilm;
    Validator validator;
    Set<ConstraintViolation<Film>> violations;

    @BeforeEach
    public void setUp() {
        film = new Film();
        film.setId(1L);
        film.setName("The Witcher");
        film.setDescription("About monsters");
        film.setReleaseDate(LocalDate.of(2020, Month.JUNE, 15));
        film.setDuration(120);
        film.setId(1L);
        newFilm = new Film();
        newFilm.setId(film.getId());
        films.clear();
        validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Test
    public void addFilmSuccessWithValidAllFields() {
        filmController.addFilm(film);
        assertEquals(1L, films.get(film.getId()).getId(), "Объект не добавлен.");
    }

    @Test
    public void addFilmUnSuccessWithEmptyName() {
        film.setName("");
        filmController.addFilm(film);
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void filmValidationWithLongDescription() {
        String originalString = "!";
        film.setDescription(originalString.repeat(210));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addFilmUnSuccessWithEarlyReleaseDate() {
        film.setReleaseDate(LocalDate.of(1890, Month.JUNE, 15));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void filmValidationWithFutureReleaseDate() {
        film.setReleaseDate(LocalDate.of(2030, Month.JUNE, 15));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void filmValidationWithNegativeDuration() {
        film.setDuration(-50);
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void updateFilmUnSuccessWithAnotherId() {
        filmController.addFilm(film);
        newFilm.setId(2L);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(newFilm));
    }

    @Test
    public void updateFilmUnSuccessWithoutId() {
        filmController.addFilm(film);
        newFilm.setId(null);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(newFilm));
    }

    @Test
    public void updateFilmUnSuccessIfFilmIdIsMissingInFilmList() {
        newFilm.setName("Billy");
        assertThrows(ValidationException.class, () -> filmController.updateFilm(newFilm));
    }

    @Test
    public void updateFilmSuccessWithAnotherName() {
        filmController.addFilm(film);
        newFilm.setName("Billy");
        filmController.updateFilm(newFilm);
        assertEquals("Billy", films.get(newFilm.getId()).getName(), "Объект не обновлен.");
    }

    @Test
    public void updateFilmSuccessWithAnotherDescription() {
        filmController.addFilm(film);
        newFilm.setDescription("123");
        filmController.updateFilm(newFilm);
        assertEquals("123", films.get(newFilm.getId()).getDescription(), "Объект не обновлен.");
    }

    @Test
    public void updateFilmSuccessWithAnotherReleaseDate() {
        filmController.addFilm(film);
        newFilm.setReleaseDate(LocalDate.of(2020, Month.DECEMBER, 31));
        filmController.updateFilm(newFilm);
        assertEquals(LocalDate.of(2020, Month.DECEMBER, 31), films.get(newFilm.getId()).getReleaseDate(), "Объект не обновлен.");
    }

    @Test
    public void updateFilmUnSuccessWithEarlyReleaseDate() {
        filmController.addFilm(film);
        newFilm.setReleaseDate(LocalDate.of(1845, Month.DECEMBER, 31));
        assertThrows(ValidationException.class, () -> filmController.updateFilm(newFilm));
    }

    @Test
    public void updateFilmSuccessWithAnotherDuration() {
        filmController.addFilm(film);
        newFilm.setDuration(200);
        filmController.updateFilm(newFilm);
        assertEquals(200, films.get(newFilm.getId()).getDuration(), "Объект не обновлен.");
    }
}