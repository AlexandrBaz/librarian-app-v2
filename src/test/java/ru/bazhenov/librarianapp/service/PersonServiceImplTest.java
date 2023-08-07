package ru.bazhenov.librarianapp.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bazhenov.librarianapp.LibrarianAppApplication;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;
import ru.bazhenov.librarianapp.repositories.PersonRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
//@DataJpaTest
@RunWith(SpringRunner.class)
public class PersonServiceImplTest {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public PersonRepositoryService personService() {
            return new PersonRepositoryServiceImpl();
        }
    }
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonRepositoryService personRepositoryService;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PersonRepositoryServiceImpl personRepositoryServiceImpl;

    Person testUser = new Person("Первый Юзер Тест", "login", "1Password@", "test@test.com",
            new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.USER,
            false, false);
    Person testUser2 = new Person("Второй Юзер Тест", "login2", "1Password@", "test2@test.com",
            new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.USER,
            true, false);
    Person testUser3 = new Person("Третий Юзер Тест", "login3", "1Password@", "test3@test.com",
            new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.USER,
            true, false);
    Person testUser4 = new Person("Четвертый Юзер Тест", "login4", "1Password@", "test4@test.com",
            new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.ADMIN,
            true, false);
    Person testUser5 = new Person("Пятый Юзер Тест", "login5", "1Password@", "test5@test.com",
            new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.ADMIN,
            true, false);

    List<Person> personList = Arrays.asList(testUser, testUser2, testUser3, testUser4, testUser5);


    @Test
    public void personByLoginIsPresent() {
        entityManager.persistAndFlush(testUser);
        Boolean found = personRepositoryService.personByLoginIsPresent(testUser.getLogin());
        assertThat(found).isTrue();
    }
//
//    @Test
//    void personByEmailIsPresent() {
//    }
//
//    @Test
//    void getAllBannedUsers() {
//    }
//
//    @Test
//    void getAllUser() {
//    }
//
//    @Test
//    void registerNewUser() {
//    }
//
//    @Test
//    void updateUser() {
//    }
//
//    @Test
//    void changePersonStatus() {
//    }
}
