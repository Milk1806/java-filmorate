package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            log.error("Поле email пустое или нет символа @.");
            throw new ValidationException("Поле email пустое или нет символа @.");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.error("Поле login пустое или содержит пробелы.");
            throw new ValidationException("Поле login пустое или содержит пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения указана в будущем.");
            throw new ValidationException("Дата рождения указана в будущем.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь с номером id {} добавлен.", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Id пользователя должно быть указано.");
            throw new ValidationException("Id пользователя должно быть указано.");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            users.remove(oldUser.getId());
            if (newUser.getEmail() != null && newUser.getEmail().contains("@")) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getLogin() != null && !(newUser.getLogin().contains(" "))) {
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getBirthday() != null && newUser.getBirthday().isBefore(LocalDate.now())) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            users.put(oldUser.getId(), oldUser);
            log.info("Пользователь с номером id {} обновлен.", oldUser.getId());
            return oldUser;
        }
        log.error("Пользователь с id {} не найден.", newUser.getId());
        throw new ValidationException("Пользователь с таким id не найден.");
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Long getNextId() {
        long currentNextId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentNextId;
    }
}
