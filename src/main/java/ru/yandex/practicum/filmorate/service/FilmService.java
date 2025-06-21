package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Getter
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addLike(Long filmId, Long userId) {
        log.info("Начало работы метода addLike.");
        Optional<Film> filmOptional = inMemoryFilmStorage.getFilmById(filmId);
        Optional<User> userOptional = inMemoryUserStorage.getUserById(userId);
        if (filmOptional.isPresent() && userOptional.isPresent()) {
            Film film = filmOptional.get();
            User user = userOptional.get();

            film.getLikes().add(user.getId());
        } else {
            log.error("Фильм для добавления лайка не найден. Исключение NotFoundException.");
            throw new NotFoundException("Фильм или пользователь не найдены.");
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        log.info("Начало работы метода deleteLike.");
        Optional<Film> filmOptional = inMemoryFilmStorage.getFilmById(filmId);
        Optional<User> userOptional = inMemoryUserStorage.getUserById(userId);

        if (filmOptional.isPresent() && userOptional.isPresent()) {
            Film film = filmOptional.get();
            User user = userOptional.get();

            film.getLikes().remove(user.getId());
        } else {
            log.error("Фильм или пользователь для удаления лайка не найден. Исключение NotFoundException.");
            throw new NotFoundException("Фильм или пользователь не найдены.");
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Начало работы метода getPopularFilms.");
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Long.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .toList();
    }
}
