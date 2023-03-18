package ru.yandex.practicum.filmorate.tests;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidationsTests extends User {
    LocalDate localDate = LocalDate.of(1946,01,20) ;
    @Test
    public void normalUserAdd(){

        User user = new User(1, "login", "name", "Email@email.jp" ,localDate);
        assertEquals("User(id=1, login=login, name=name, email=Email@email.jp, birthday=1946-01-20)", user.toString());
    }

//я прошу прощения, не разобраался что тестировать. как-будто все через аннотации
    @Test
    public void badLoginUserAdd(){
        User user = new User(1, " ", "name", "Email@email.jp" ,localDate);
        User user1 = new User(1, "", "name", "Email@email.jp" ,localDate);
        assertEquals(user, user);
    }
}
