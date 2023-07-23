package ru.bazhenov.librarianapp.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;
import ru.bazhenov.librarianapp.repositories.PersonRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonServiceImplIntegrationTest {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public PersonService personService() {
            return new PersonServiceImpl();
        }
    }

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        Person john = new Person("Первый Юзер Тест", "login", "1Password@", "test@test.com",
                new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.USER,
                false, false);
        john.setId(11L);
        Person bob = new Person("Второй Юзер Тест", "login2", "1Password@", "test2@test.com",
                new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.USER,
                true, false);
        Person alex = new Person("Третий Юзер Тест", "login3", "1Password@", "test3@test.com",
                new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.USER,
                true, false);
        Person admin = new Person("Четвертый Юзер Тест", "login4", "1Password@", "test4@test.com",
                new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.ADMIN,
                true, false);
        Person superAdmin = new Person("Пятый Юзер Тест", "login5", "1Password@", "test5@test.com",
                new GregorianCalendar(2016, Calendar.MARCH, 6).getTime(), null, PersonRole.ADMIN,
                false, false);

        List<Person> allPerson = Arrays.asList(john, bob, alex, admin, superAdmin);

        Mockito.when(personRepository.getReferenceById(john.getId())).thenReturn(john);
        Mockito.when(personRepository.findById(-99L)).thenReturn(Optional.empty());
        Mockito.when(personRepository.findPersonByLogin(john.getLogin())).thenReturn(Optional.of(john));
        Mockito.when(personRepository.findPersonByLogin(alex.getLogin())).thenReturn(Optional.of(alex));
        Mockito.when(personRepository.findPersonByLogin("wrong_name")).thenReturn(null);

        Mockito.when(personService.personByLoginIsPresent(john.getLogin())).thenReturn(true);

//        Mockito.when(personService.personByLoginIsPresent(john.getLogin())).thenReturn(personRepository.findPersonByLogin(john.getLogin()).isPresent()).thenReturn(true);

//        Mockito.when(personRepository.findPersonByLogin("wrong_name").isPresent()).thenReturn(false);

//        Mockito.when(personRepository.f("wrong_name").isPresent()).thenReturn(false);

        Mockito.when(personRepository.findAll()).thenReturn(allPerson);

    }
    @Test
    public void whenValidId_thenPersonShouldBeFound(){
        Person fromDb = personService.getPersonById(11L);
        assertThat(fromDb.getFullName()).isEqualTo("Первый Юзер Тест");

        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void whenInValidId_thenPersonShouldNotBeFound() {
        Person fromDb = personService.getPersonById(-99L);
        verifyFindByIdIsCalledOnce();
        assertThat(fromDb).isNull();
    }

    @Test
    public void  whenValidLogin_thenPersonShouldBeFound() {
        String login = "login";
        Person found = personService.getPersonByLogin(login);
        assertThat(found.getLogin()).isEqualTo(login);
    }

    @Test
    public void whenInValidLogin_thenPersonShouldNotBeFound() {
        Person fromDb = personService.getPersonByLogin("wrong_login");
        assertThat(fromDb).isNull();

        verifyFindByLoginIsCalledOnce("wrong_login");
    }

    @Test
    public void whenValidLogin_ThenPersonShouldIsPresent() {
        String login = "login";
        Boolean fromDb = personService.personByLoginIsPresent(login);
        assertThat(fromDb).isNotNull();
    }

    private void verifyFindByLoginIsCalledOnce(String login) {
        Mockito.verify(personRepository, VerificationModeFactory.times(1)).findPersonByLogin(login);
        Mockito.reset(personRepository);
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(personRepository, VerificationModeFactory.times(1)).getReferenceById(Mockito.anyLong());
        Mockito.reset(personRepository);
    }
}
