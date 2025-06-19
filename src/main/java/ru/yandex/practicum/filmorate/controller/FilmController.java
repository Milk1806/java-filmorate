package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("В метод addFilm передан параметр {}", film);
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.debug("В метод updateFilm передан параметр {}", newFilm);
        return filmStorage.updateFilm(newFilm);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.debug("В метод getFilmById передан параметр {}", id);
        return filmStorage.getFilmById(id).get();
    }

    @DeleteMapping
    public void deleteFilm(@Valid @RequestBody Film film) {
        log.debug("В метод deleteFilm передан параметр {}", film);
        filmStorage.deleteFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("В метод addLike переданы параметр {}, {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("В метод deleteLike переданы параметр {}, {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        log.debug("В метод getPopularFilms передан параметр {}", count);
        return filmService.getPopularFilms(count);
    }
}
