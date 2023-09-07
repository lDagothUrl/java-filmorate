package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {

    List<Mpa> getListMpa();

    Mpa getMpaById(int mpaId);
}