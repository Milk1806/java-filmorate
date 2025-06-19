package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@Getter
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь с номером id {} добавлен.", user.getId());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
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
        throw new NotFoundException("Пользователь с таким id не найден.");
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
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
