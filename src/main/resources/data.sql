INSERT INTO MPA (name) VALUES ('G');
INSERT INTO MPA (name) VALUES ('PG');
INSERT INTO MPA (name) VALUES ('PG-13');
INSERT INTO MPA (name) VALUES ('R');
INSERT INTO MPA (name) VALUES ('NC-17');

INSERT INTO GENRE (name) VALUES ('Комедия');
INSERT INTO GENRE (name) VALUES ('Драма');
INSERT INTO GENRE (name) VALUES ('Мультфильм');
INSERT INTO GENRE (name) VALUES ('Триллер');
INSERT INTO GENRE (name) VALUES ('Документальный');
INSERT INTO GENRE (name) VALUES ('Боевик');

INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES ('Фильма111', 'Описание111', '2000-01-01', 100, 1);
INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES ('Фильма222', 'Описание222', '2015-11-11', 150, 2);
INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES ('Фильма333', 'Описание333', '2003-03-03', 120, 1);

INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ('1@ya.ru', 'логин', 'имя', '2000-01-01');
INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ('2@ya.ru', 'логин22', 'имя22', '2002-01-01');
INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ('13@ya.ru', 'логин33', 'имя33', '2004-01-01');

INSERT INTO LIKES_FILM (FILM_ID, USER_ID) VALUES (1, 1);
INSERT INTO LIKES_FILM (FILM_ID, USER_ID) VALUES (1, 2);
INSERT INTO LIKES_FILM (FILM_ID, USER_ID) VALUES (2, 1);

INSERT INTO FRIENDS_USER (USER_ID, FRIENDS_ID, FRIENDSHIP) VALUES (1, 2, 'true');
INSERT INTO FRIENDS_USER (USER_ID, FRIENDS_ID, FRIENDSHIP) VALUES (2, 1, 'true');

INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (1, 1);
INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (1, 2);
INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (2, 2);
INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (3, 4);
INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (3, 5);
INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (3, 6);