package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("В метод addFilm передан параметр {}", film);
        return filmService.getInMemoryFilmStorage().addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.debug("В метод updateFilm передан параметр {}", newFilm);
        return filmService.getInMemoryFilmStorage().updateFilm(newFilm);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getInMemoryFilmStorage().getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.debug("В метод getFilmById передан параметр {}", id);
        Optional<Film> filmOptional = filmService.getInMemoryFilmStorage().getFilmById(id);
        if (filmOptional.isPresent()) {
            return filmOptional.get();
        } else {
            throw new NotFoundException("Фильм с необходимым id не найден.");
        }
    }

    @DeleteMapping
    public void deleteFilm(@Valid @RequestBody Film film) {
        log.debug("В метод deleteFilm передан параметр {}", film);
        filmService.getInMemoryFilmStorage().deleteFilm(film);
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
