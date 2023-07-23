package ru.bazhenov.librarianapp.repositories;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PersonRepository personRepository;

    Person testUser = new Person("Первый Юзер Тест", "login", "1Password@", "test@test.com",
            new GregorianCalendar(2016, Calendar.MARCH,6).getTime(), null,PersonRole.USER,
            false,false);
    Person testUser2 = new Person("Второй Юзер Тест", "login2", "1Password@", "test2@test.com",
            new GregorianCalendar(2016,Calendar.MARCH,6).getTime(), null,PersonRole.USER,
            true,false);
    Person testUser3 = new Person("Третий Юзер Тест", "login3", "1Password@", "test3@test.com",
            new GregorianCalendar(2016,Calendar.MARCH,6).getTime(), null,PersonRole.USER,
            true,false);
    Person testUser4 = new Person("Четвертый Юзер Тест", "login4", "1Password@", "test4@test.com",
            new GregorianCalendar(2016,Calendar.MARCH,6).getTime(), null,PersonRole.ADMIN,
            true,false);

    Person testUser5 = new Person("Пятый Юзер Тест", "login5", "1Password@", "test5@test.com",
            new GregorianCalendar(2016,Calendar.MARCH,6).getTime(), null,PersonRole.ADMIN,
            true,false);

    @Test
    void whenFindPersonByLogin_thenReturnPerson() {
        entityManager.persistAndFlush(testUser);
        Person found = personRepository.findPersonByLogin(testUser.getLogin()).orElse(null);
        assertThat(Objects.requireNonNull(found).getLogin()).isEqualTo(testUser.getLogin());
    }

    @Test
    public void whenInvalidName_thenReturnNull(){
        Person fromDb = personRepository.findPersonByLogin("ErrorLogin").orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindPersonByEmail_thenReturnPerson() {
        entityManager.persistAndFlush(testUser);
        Person found = personRepository.findPersonByEmail(testUser.getEmail()).orElse(null);
        assertThat(Objects.requireNonNull(found).getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void whenFindPersonByEmail_thenReturnNull() {
        entityManager.persistAndFlush(testUser);
        Person found = personRepository.findPersonByEmail("wrong@email.com").orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfPerson_whenFindAllByPersonRoleAndIsBanned_thenReturnAllPersons() {
        entityManager.persist(testUser);
        entityManager.persist(testUser2);
        entityManager.persist(testUser3);
        entityManager.persist(testUser4);
        entityManager.persist(testUser5);
        entityManager.flush();

        List<Person> personList = personRepository.findAllByPersonRoleAndIsBanned(PersonRole.USER, true);
        assertThat(personList).hasSize(2).extracting(Person::getFullName).containsOnly(testUser2.getFullName(), testUser3.getFullName());
    }

    @Test
    void givenSetOfPerson_whenFindAllByPersonRole_thenReturnAllPersons() {
        entityManager.persist(testUser);
        entityManager.persist(testUser2);
        entityManager.persist(testUser3);
        entityManager.persist(testUser4);
        entityManager.persist(testUser5);
        entityManager.flush();

        List<Person> personList = personRepository.findAllByPersonRole(PersonRole.ADMIN);
        assertThat(personList).hasSize(2).extracting(Person::getFullName).containsOnly(testUser4.getFullName(), testUser5.getFullName());
    }
}