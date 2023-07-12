drop table person_book;
drop table person;
drop table book;

CREATE TABLE person (
                        id bigint generated by default as identity primary key unique ,
                        full_name varchar(100) UNIQUE NOT NULL ,
                        "login" varchar (100) UNIQUE NOT NULL ,
                        "password" varchar (100) NOT NULL ,
                        email varchar (100) UNIQUE NOT NULL,
                        year_of_birth date NOT NULL,
                        person_role varchar (20),
                        is_baned boolean not null ,
                        pass_expired boolean not null
);

CREATE TABLE book (
                      id bigint generated by default as identity primary key ,
                      "name" varchar(100) not null ,
                      author varchar(100)  not null ,
                      "year" varchar not null ,
                      books_count bigint not null
);


CREATE TABLE person_book (
                             id bigint generated by default as identity primary key unique ,
                             person_book_date date not null ,
                             book bigint references book(id) on delete RESTRICT,
                             person bigint references person(id) on delete RESTRICT
);



insert into person (id, full_name, "login", "password", email, year_of_birth, person_role, is_baned, pass_expired)
values  (1, 'Админ Админович Админов', 'admin', '$2a$10$DlFeQQyrMOxyh8r6ZOwMy.44e4.Pl5DjapV2V75F8qp3LVaM.3lfC', 'admin@adminovich.com', '2021-07-05', 'ADMIN', false, false),
        (3, 'Юзер Юзерович Два', 'user2', '$2a$10$wyTIwEFqi5CqwfOniFmgw.sAwbXIXrtQwAJDYgJidSHKnXcxv4QDu', 'user2@user2.com', '2019-07-08', 'USER', false, false),
        (4, 'Юзер Юзерович Три', 'user3', '$2a$10$lm0X4hRrPFtcJ/30vta9Ee.ccprguQrC0dhM8sD3ejbZ2fmrwCNBi', 'user3@user3.com', '2017-07-02', 'USER', false, false),
        (6, 'Бан Баннович Баннанов', 'banned', '$2a$10$ntaE4CfRP9kPQcXq2HL8se8NP31gxLS2jRrZXuH0b0b53Sv1YBOOm', 'banned@banned.com', '2017-07-12', 'USER', true, false),
        (5, 'Менеджер Менеджерович Менеджеров', 'manager', '$2a$10$6YpthYF0RwOJzS7c4rSHeOy/c5eCd3B4dQ009EVJd1hn9IZVwOJha', 'manager@manager.com', '2018-07-17', 'MANAGER', false, false),
        (2, 'Юзер Юзерович Один', 'user', '$2a$10$Hw74MFCP8YgX.ShD3Us3eeltUHdS9EjKxMXZ.y9VGXZ6J8au8DVKi', 'user@user.com', '2019-07-01', 'USER', false, false),
        (11, 'Менеджер Менеджероч Второй', 'manager2', '$2a$10$k/dLJvGSYevBYHQ6bdEJueQ/iUu3KqBY7gqSzHMmbtu8e9IJVbqlO', 'manager2@manager2.com', '2019-03-06', 'MANAGER', true, false);

insert into book (id, "name", author, "year", books_count)
values  (4, 'Мёртвые души', 'Николай Гоголь', '1842', 7),
        (8, 'Отверженные', 'Виктор Гюго', '1862', 9),
        (15, 'Отцы и дети', 'Иван Тургенев', '1860', 9),
        (17, 'Палата № 6', 'Антон Чехов', '1892', 9),
        (12, 'Повести Белкина', 'Александр Пушкин', '1831', 9),
        (10, 'Преступление и наказание', 'Федор Достоевский', '1866', 10),
        (13, 'Ревизор', 'Николай Гоголь', '1836', 10),
        (14, 'Село Степанчиково и его обитатели', 'Федор Достоевский', '1859', 10),
        (7, 'Три товарища', 'Эрих Мария Ремарк', '1936', 8),
        (16, 'Три мушкетера', 'Александр Дюма', '1844', 8),
        (1, 'Мастер и Маргарита', 'Михаил Булгаков', '1929', 7),
        (3, 'Двенадцать стульев', 'Илья Ильф, Евгений Петров', '1928', 5),
        (2, 'Собачье сердце', 'Михаил Булгаков', '1925', 5),
        (6, 'Золотой теленок', 'Илья Ильф, Евгений Петров', '1931', 9),
        (18, 'Братья Карамазовы', 'Федор Достоевский', '1879', 9),
        (5, 'Граф Монте-Кристо', 'Александр Дюма', '1844', 5),
        (11, 'Война и мир', 'Лев Толстой', '1865', 9),
        (22, 'Работодать ты где или как приручить ЭЙЧАР', 'Джун Джунович Джунов', '2023', 9),
        (20, 'Идиот', 'Федор Достоевский', '1868', 0),
        (21, 'Приключения Джуна в России', 'Джун Джунович Джунов', '2022-2023', 9),
        (9, 'Евгений Онегин', 'Александр Пушкин', '1825', 10),
        (19, 'Собака Баскервилей', 'Артур Конан Дойль', '1901', 0);

insert into person_book (id, person_book_date, book, person)
values  (6, '2023-07-07', 16, 3),
        (7, '2023-07-07', 3, 3),
        (9, '2023-07-07', 7, 3),
        (10, '2023-07-07', 20, 4),
        (12, '2023-07-07', 16, 4),
        (16, '2023-07-07', 15, 3),
        (17, '2023-07-07', 2, 3),
        (18, '2023-07-07', 19, 4),
        (11, '2023-01-03', 17, 4),
        (19, '2023-05-27', 12, 4),
        (33, '2023-07-10', 18, 2),
        (34, '2023-07-10', 5, 2),
        (35, '2023-07-10', 11, 2),
        (38, '2023-01-01', 21, 2),
        (37, '2023-01-01', 22, 2);