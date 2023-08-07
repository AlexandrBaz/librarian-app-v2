package ru.bazhenov.librarianapp.service;

import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.util.List;
@Service()
public interface PersonRepositoryService{
    Person getPersonById(long id);
    Person getPersonByLogin(String login);
    Boolean personByLoginIsPresent(String login);
    Boolean PersonByEmailIsPresent(String email);
    List<Person> getAllBannedUsers();
    List<Person> getAllUser(PersonRole user);
    void registerNewUser(PersonDto personDto, PersonRole personRole);
    void updateUser(ChangePersonDto personDto);
    void changePersonStatus(Person person);
}
