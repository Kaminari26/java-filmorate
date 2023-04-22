package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.Mpa.IMpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaRatingController {

    IMpaService mpaService;

    @Autowired
    public MpaRatingController(IMpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public MpaRating getRating(@PathVariable int id) {
        log.info("Пришел запрос /mpa/{id}");
        MpaRating mpaRating = mpaService.getRatingId(id);
        log.info("ответ " + mpaRating);
        return mpaRating;
    }

    @GetMapping
    public Collection<MpaRating> getAllRating() {
        log.info("Пришел запрос /mpa");
        Collection<MpaRating> mpaRatings = mpaService.getAllMpa();
        log.info("ответ " + mpaRatings);
        return mpaRatings;
    }

}
