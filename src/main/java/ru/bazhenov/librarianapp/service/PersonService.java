package ru.bazhenov.librarianapp.service;

import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.util.List;
@Service
public interface PersonService {
    public Person getPersonById(long id);
    public Person getPersonByLogin(String login);
    public Boolean personByLoginIsPresent(String login);
    public Boolean PersonByEmailIsPresent(String email);
    public List<Person> getAllBannedUsers();
    public List<Person> getAllUser(PersonRole user);
    public void registerNewUser(PersonDto personDto, PersonRole personRole);
    public void updateUser(ChangePersonDto personDto);
    public void changePersonStatus(Person person);
}
