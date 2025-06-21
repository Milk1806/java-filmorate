package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Getter
public class UserService {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addUserAsFriend(Long userId, Long friendId) {
        log.info("Начало работы метода addUserAsFriend.");
        Optional<User> userOptional = inMemoryUserStorage.getUserById(userId);
        Optional<User> friendOptional = inMemoryUserStorage.getUserById(friendId);

        if (userOptional.isPresent() && friendOptional.isPresent()) {
            User user = userOptional.get();
            User friend = friendOptional.get();

            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
        } else {
            log.error("Пользователь для добавления в друзья не найден. Исключение NotFoundException.");
            throw new NotFoundException("Один или оба пользователя не найдены.");
        }
    }

    public void removeUnfriend(Long userId, Long friendId) {
        log.info("Начало работы метода removeUnfriend.");
        Optional<User> userOptional = inMemoryUserStorage.getUserById(userId);
        Optional<User> friendOptional = inMemoryUserStorage.getUserById(friendId);

        if (userOptional.isPresent() && friendOptional.isPresent()) {
            User user = userOptional.get();
            User friend = friendOptional.get();
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
        } else {
            log.error("Пользователя нет в списке друзей для удаления из списка друзей. Исключение NotFoundException.");
            throw new NotFoundException("Один или оба пользователя не найдены.");
        }
    }

    public List<User> getGeneralFriendsList(Long id, Long otherId) {
        log.info("Начало работы метода getGeneralFriendsList.");
        if (inMemoryUserStorage.getUserById(id).isPresent() && inMemoryUserStorage.getUserById(otherId).isPresent()) {
            return inMemoryUserStorage.getUserById(id).get().getFriends().stream()
                    .filter(inMemoryUserStorage.getUserById(otherId).get().getFriends()::contains)
                    .map(friendId -> findUserById(friendId, inMemoryUserStorage.getAllUsers()).orElse(null))
                    .toList();
        }
        log.error("Оба или один из пользователей не найден. Исключение NotFoundException.");
        throw new NotFoundException("Пользователь не найден.");
    }

    public List<User> getAllFriendsList(Long userId) {
        log.info("Начало работы метода getAllFriendsList.");
        if (inMemoryUserStorage.getUserById(userId).isPresent()) {
            return inMemoryUserStorage.getUserById(userId).get().getFriends().stream()
                    .map(friendId -> findUserById(friendId, inMemoryUserStorage.getAllUsers()).orElse(null))
                    .toList();
        }
        log.error("Пользователь со списком друзей не найден. Исключение NotFoundException.");
        throw new NotFoundException("Пользователь не найден.");
    }

    private Optional<User> findUserById(Long friendId, Collection<User> users) {
        log.info("Начало работы метода findUserById.");
        return users.stream()
                .filter(user -> friendId.equals(user.getId()))
                .findFirst();
    }
}
