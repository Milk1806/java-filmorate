package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();
    private final Map<Long, User> users = userController.getUsers();
    private User user;
    private User newUser;
    Validator validator;
    Set<ConstraintViolation<User>> violations;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Max");
        user.setBirthday(LocalDate.of(1991, Month.JUNE, 23));
        user.setEmail("maxlebedev22@yandex.ru");
        user.setLogin("Milk1806");
        newUser = new User();
        newUser.setId(user.getId());
        users.clear();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void addUserSuccessWithValidAllFields() {
        userController.addUser(user);
        assertEquals(1L, users.get(user.getId()).getId(), "Объект не добавлен.");
    }

    @Test
    public void addUserSuccessWithEmptyName() {
        user.setName(null);
        userController.addUser(user);
        assertEquals("Milk1806", users.get(user.getId()).getName(), "В поле name не присваивается login.");
    }

    @Test
    public void userValidationWithEmptyEmail() {
        user.setEmail(null);
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userValidationWithoutCharDogInEmail() {
        user.setEmail("maxlebedev22yandex.ru");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userValidationWithEmptyLogin() {
        user.setLogin(null);
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userValidationWithEmptySpaceInLogin() {
        user.setLogin("Milk1806 ");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void userValidationWithBirthdayAfterNowLocalDate() {
        user.setBirthday(LocalDate.of(2026, Month.JUNE, 23));
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void updateUserUnSuccessWithAnotherId() {
        userController.addUser(user);
        newUser.setId(2L);
        assertThrows(ValidationException.class, () -> userController.updateUser(newUser));
    }

    @Test
    public void updateUserUnSuccessWithoutId() {
        userController.addUser(user);
        newUser.setId(null);
        assertThrows(ValidationException.class, () -> userController.updateUser(newUser));
    }

    @Test
    public void updateUserUnSuccessIfUserIdIsMissingInUsersList() {
        newUser.setName("Kate");
        assertThrows(ValidationException.class, () -> userController.updateUser(newUser));
    }

    @Test
    public void updateUserSuccessWithAnotherName() {
        userController.addUser(user);
        newUser.setName("Kate");
        userController.updateUser(newUser);
        assertEquals("Kate", users.get(newUser.getId()).getName(), "Объект не обновлен.");
    }

    @Test
    public void updateUserUnSuccessIfNameIsAbsent() {
        userController.addUser(user);
        userController.updateUser(newUser);
        assertEquals("Max", users.get(newUser.getId()).getName(), "Объект не обновлен.");
    }

    @Test
    public void updateUserSuccessWithAnotherEmail() {
        userController.addUser(user);
        newUser.setEmail("123@yandex.ru");
        newUser = userController.updateUser(newUser);
        assertEquals("123@yandex.ru", users.get(newUser.getId()).getEmail(), "Объект не обновлен.");
    }

    @Test
    public void updateUserSuccessWithAnotherLogin() {
        userController.addUser(user);
        newUser.setLogin("123");
        newUser = userController.updateUser(newUser);
        assertEquals("123", users.get(newUser.getId()).getLogin(), "Объект не обновлен.");
    }

    @Test
    public void updateUserSuccessWithAnotherBirthday() {
        userController.addUser(user);
        newUser.setBirthday(LocalDate.of(2000, Month.JUNE, 1));
        newUser = userController.updateUser(newUser);
        assertEquals(LocalDate.of(2000, Month.JUNE, 1), users.get(newUser.getId()).getBirthday(), "Объект не обновлен.");
    }
}