package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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
    public User addUser(@Valid @RequestBody User user) {
        validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь с номером id {} добавлен.", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            if (newUser.getName() != null) {
                validate(newUser);
                oldUser.setName(newUser.getName());
            }
            if (newUser.getBirthday() != null) {
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

    public void validate(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
