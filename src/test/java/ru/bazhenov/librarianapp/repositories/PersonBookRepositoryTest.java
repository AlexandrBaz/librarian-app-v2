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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@DataJpaTest
class PersonBookRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonBookRepository personBookRepository;

    Book testBook = new Book("Тестовая книга", "Автор", "2015", 10, null);
    Person testUser = new Person("Первый Юзер Тест", "login", "1Password@", "test@test.com",
            new GregorianCalendar(2016, Calendar.MARCH,6).getTime(), null, PersonRole.USER,
            false,false);
    PersonBook personBook = new PersonBook(LocalDate.now(), testBook, testUser);

    @Test
    void whenFindFirstByPersonAndBook_ThenReturnPersonBook() {
        entityManager.persist(testBook);
        entityManager.persist(testUser);
        entityManager.persist(personBook);
        entityManager.flush();
        PersonBook found = personBookRepository.findFirstByPersonAndBook(testUser,testBook).orElse(null);
        assertThat(Objects.requireNonNull(found).getPersonBookDate()).isEqualTo(personBook.getPersonBookDate());

    }
}