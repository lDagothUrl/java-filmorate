package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Map<Long, Film> films = new HashMap<>();
    private static final Map<Long, Set<Long>> filmsLike = new HashMap<>();

    private long generatorId = 0;

    private long generateId() {
        return ++generatorId;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("filmId: " + id);
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilm(Integer count) {
        List<Film> popularFilm = new ArrayList<>();
        final Map<Long, Integer> sortSize = new LinkedHashMap<>();
        for (Long filmId : filmsLike.keySet()) {
            sortSize.put(filmId, filmsLike.get(filmId).size());
        }
        int flag = count != null ? count : 10;
        Map<Long, Integer> sorted =
                sortSize.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue((o1, o2) -> {
                            return o2 - o1;
                        })).limit(flag).collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (Long filmId : sorted.keySet()) {
            popularFilm.add(films.get(filmId));
        }
        return popularFilm;
    }

    public Film postFilms(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        filmsLike.put(film.getId(), new HashSet<>());
        return film;
    }

    public Film putFilms(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("FilmId: " + film.getId());
        }
    }

    @Override
    public void delFilms() {
        films.clear();
        filmsLike.clear();
    }

    @Override
    public Film delFilm(Long id) {
        Film film = films.remove(id);
        filmsLike.remove(id);
        return film;
    }

    @Override
    public void addFilmsLike(Long filmId, Long userId) {
        if (films.get(filmId) == null) {
            throw new NotFoundException("Not found film filmId = " + filmId);
        }
        Set<Long> userIdLike = filmsLike.computeIfAbsent(filmId, id -> new HashSet<>());
        userIdLike.add(userId);
    }

    @Override
    public void delFilmsLike(Long filmId, Long userId) {
        Set<Long> userIdLike = filmsLike.get(filmId);
        if (userIdLike == null) {
            throw new NotFoundException("filmId: " + filmId);
        }
        if (!userIdLike.contains(userId)) {
            throw new NotFoundException("userId: " + userId);
        }
        userIdLike.remove(userId);
    }
}
