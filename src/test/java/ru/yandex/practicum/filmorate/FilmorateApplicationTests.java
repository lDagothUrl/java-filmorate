
package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeDao;
import ru.yandex.practicum.filmorate.storage.user.FriendListDaoImpl;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FriendListDaoImpl friendListDaoImpl;
    @Mock
    private LikeDao likeDao;
    @Mock
    private FilmStorage filmStorage;
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filmService = new FilmService(filmStorage, likeDao);
    }

    private Film initializeDataFilm() {
        Mpa sampleMpa = new Mpa();
        sampleMpa.setId(1);

        Film sampleFilm = new Film();
        sampleFilm.setId(1);
        sampleFilm.setName("Test Film");
        sampleFilm.setDescription("This is a test film");
        sampleFilm.setReleaseDate(LocalDate.of(2023, 8, 24));
        sampleFilm.setMpa(sampleMpa);

        return sampleFilm;
    }

    private User initializeDataUser() {
        User user = new User("test@test.com", "test", "Test User", LocalDate.of(1995, 12, 18), 1);
        return userStorage.createUser(user);
    }

    @Test
    public void testCreateUser() {
        User user = initializeDataUser();
        assertThat(user.getId()).isNotNull();
        Assertions.assertThat(userStorage.findUser(user.getId())).isEqualTo(user);
    }


    @Test
    public void testCreateFilm() {
        Mpa sampleMpa = new Mpa();
        sampleMpa.setId(1);

        Film sampleFilm = new Film();
        sampleFilm.setId(1);
        sampleFilm.setName("Test Film");
        sampleFilm.setDescription("This is a test film");
        sampleFilm.setReleaseDate(LocalDate.of(2023, 8, 24));
        sampleFilm.setMpa(sampleMpa);

        when(filmStorage.createFilm(any(Film.class))).thenReturn(sampleFilm);

        Film createdFilm = filmStorage.createFilm(sampleFilm);

        verify(filmStorage, times(1)).createFilm(any(Film.class));
        assert sampleFilm.getName().equals(createdFilm.getName());
        assert sampleFilm.getDescription().equals(createdFilm.getDescription());
    }

    @Test
    public void testFindAllFilms() {
        testCreateFilm();
        Collection<Film> films = filmService.findAll();
        assertThat(films).isNotNull();
    }

    @Test
    public void testGetCommonFriends() {
        User friend1 = new User("friend1@test.com", "friend1", "Friend1", LocalDate.of(2000, 1, 1), 2);
        User friend2 = new User("friend2@test.com", "friend2", "Friend2", LocalDate.of(2000, 7, 1), 3);
        User friend3 = new User("friend3@test.com", "friend3", "Friend3", LocalDate.of(2000, 7, 1), 4);

        userStorage.createUser(friend1);
        userStorage.createUser(friend2);
        userStorage.createUser(friend3);

        friendListDaoImpl.addFriend(friend1.getId(), friend3.getId());
        friendListDaoImpl.addFriend(friend2.getId(), friend3.getId());
        Collection<User> commonFriends = friendListDaoImpl.getCommonFriends(friend1.getId(), friend2.getId());

        assertThat(commonFriends)
                .isNotNull()
                .hasSize(1)
                .containsExactly(friend3);
    }

    @Test
    public void testAddFriend() {
        User friend1 = new User("friend12@test.com", "friend12", "Friend1", LocalDate.of(2000, 1, 1), 5);
        User friend2 = new User("friend23@test.com", "friend23", "Friend2", LocalDate.of(2000, 7, 1), 6);
        userStorage.createUser(friend1);
        userStorage.createUser(friend2);

        friendListDaoImpl.addFriend(friend1.getId(), friend2.getId());
        Collection<User> friends = friendListDaoImpl.getAll(friend1.getId());

        assertThat(friends)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    public void testAddFriendSameIdsThrowsValidationException() {
        int userId = 1;
        int friendId = 1;
        assertThatThrownBy(() -> friendListDaoImpl.addFriend(userId, friendId))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Id пользователей не должны совпадать!");
    }

    @Test
    public void testAddFriendNonExistingIdsThrowsUserNotFoundException() {
        int userId = 1;
        int friendId = 999;
        assertThatThrownBy(() -> friendListDaoImpl.addFriend(userId, friendId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь не найден");
    }

    @Test
    public void testDeleteFriend() {
        User friend1 = new User("friend13@test.com", "friend13", "Friend1", LocalDate.of(2000, 1, 1), 7);
        User friend2 = new User("friend24@test.com", "friend24", "Friend2", LocalDate.of(2000, 7, 1), 8);
        userStorage.createUser(friend1);
        userStorage.createUser(friend2);

        friendListDaoImpl.addFriend(friend1.getId(), friend2.getId());
        friendListDaoImpl.deleteFriend(friend1.getId(), friend2.getId());
        Collection<User> friends = friendListDaoImpl.getAll(friend1.getId());

        assertThat(friends)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    public void testDeleteFriendNonExistingIdsThrowsValidationException() {
        User friend1 = new User("friend15@test.com", "friend15", "Friend1", LocalDate.of(2000, 1, 1), 9);
        userStorage.createUser(friend1);


        assertThatThrownBy(() -> friendListDaoImpl.deleteFriend(friend1.getId(), 8))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Введен некорректный id");
    }

    @Test
    public void testUpdateFilm() {
        Film sampleFilm = initializeDataFilm();
        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Updated Film Name");
        updatedFilm.setDescription("Updated film description");
        updatedFilm.setReleaseDate(LocalDate.of(2022, 8, 25));
        updatedFilm.setDuration(120);

        when(filmStorage.findFilm(anyInt())).thenReturn(sampleFilm);
        when(filmStorage.updateFilm(any(Film.class))).thenReturn(updatedFilm);

        Film result = filmService.updateFilm(sampleFilm);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();

        assertThat(updatedFilm).isNotNull();


        assertThat(result.getName()).isEqualTo("Updated Film Name");
        assertThat(result.getDescription()).isEqualTo("Updated film description");
        assertThat(result.getReleaseDate()).isEqualTo(LocalDate.of(2022, 8, 25));
        assertThat(result.getDuration()).isEqualTo(120);
    }


}

