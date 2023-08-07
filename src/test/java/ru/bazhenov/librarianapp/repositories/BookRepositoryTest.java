package ru.bazhenov.librarianapp.repositories;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonBook;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    Book testBook = new Book("Первая книга", "`aad'", "2015", 10, null);
    Book testBook2 = new Book("Вторая книга", "Автор книга", "2015", 10, null);
    Book testBook3 = new Book("Третья книга", "Автор книга2", "2015", 10, null);
    Person testUser = new Person("Первый Юзер Тест", "login", "1Password@", "test@test.com",
            new GregorianCalendar(2016, Calendar.MARCH,6).getTime(), null, PersonRole.USER,
            false,false);
    PersonBook personBook = new PersonBook(LocalDate.now(), testBook, testUser);
    PersonBook personBook2 = new PersonBook(LocalDate.now(), testBook2, testUser);
    List<PersonBook> personBookList = new ArrayList<>();
    List<PersonBook> personBookList2 = new ArrayList<>();
    List<PersonBook> personBookList3 = new ArrayList<>();
    @Test
    void findAvailableBookForUser() {
        testBook.setAuthor("Автор от сеттера");
        personBookList.add(personBook);
        personBookList2.add(personBook2);
        personBookList3.add(personBook);
        personBookList3.add(personBook2);
        testBook.setPersonBookList(personBookList);
        testBook.setPersonBookList(personBookList2);
        testUser.setPersonBookList(personBookList3);

        entityManager.persist(testUser);
        entityManager.persist(testBook);
        entityManager.persist(testBook2);
        entityManager.persist(testBook3);
        entityManager.persist(personBook);
        entityManager.persist(personBook2);
        entityManager.flush();

        List<Book> bookListForPerson = bookRepository.findAvailableBookForUser(0L, testUser.getId());
        assertThat(bookListForPerson).hasSize(1).extracting(Book::getName).containsOnly(testBook3.getName());
    }
}

//Почему-то не может конвертировать автора книги