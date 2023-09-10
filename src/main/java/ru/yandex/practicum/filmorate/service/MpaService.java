package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaDao;

import java.util.List;

@Service
public class MpaService {
    private final MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getListMpa() {
        return mpaDao.getListMpa();
    }

    public Mpa getMpa(int id) {
        return mpaDao.getMpaById(id);
    }
}