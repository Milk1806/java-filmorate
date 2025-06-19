package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User newUser);
    void deleteUser(User user);
    Collection<User> getAllUsers();
    Optional<User> getUserById(Long id);

}
