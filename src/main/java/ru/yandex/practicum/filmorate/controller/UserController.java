package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.debug("В метод addUser передан параметр {}", user);
        return userService.getInMemoryUserStorage().addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.debug("В метод updateUser передан параметр {}", newUser);
        return userService.getInMemoryUserStorage().updateUser(newUser);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getInMemoryUserStorage().getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.debug("В метод getUserById передан параметр {}", id);
        Optional<User> optionalUser = userService.getInMemoryUserStorage().getUserById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("Пользователь с необходимым if не найден.");
        }
    }

    @DeleteMapping
    public void deleteUser(@Valid @RequestBody User user) {
        log.debug("В метод deleteUser передан параметр {}", user);
        userService.getInMemoryUserStorage().deleteUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addUserAsFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("В метод addUserAsFriend переданы параметр {}, {}", id, friendId);
        userService.addUserAsFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeUnfriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("В метод removeUnfriend переданы параметр {}, {}", id, friendId);
        userService.removeUnfriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriendsList(@PathVariable Long id) {
        log.debug("В метод getAllFriendsList передан параметр {}", id);
        return userService.getAllFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getGeneralFriendsList(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("В метод getGeneralFriendsList переданы параметр {}, {}", id, otherId);
        return userService.getGeneralFriendsList(id, otherId);
    }
}
