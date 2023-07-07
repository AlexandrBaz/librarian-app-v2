package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;
import ru.bazhenov.librarianapp.repositories.PersonRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person getPerson(long id){return personRepository.getReferenceById(id);}

    public Person getPersonByLogin(String login){
        return personRepository.findPersonByLogin(login).orElse(null);
    }

    public Boolean personByLoginIsPresent(String login) {
        return personRepository.findPersonByLogin(login).isPresent();
    }

    public Boolean PersonByEmailIsPresent(String email) {
        return personRepository.findPersonByEmail(email).isPresent();
    }

    @Transactional
    public void registerNewUser(PersonDto personDto) {
        Person newPerson = new Person();
        newPerson.setFullName(personDto.getFullName());
        newPerson.setLogin(personDto.getLogin());
        newPerson.setPassword(passwordEncoder.encode(personDto.getPassword()));
        newPerson.setEmail(personDto.getEmail());
        newPerson.setYearOfBirth(personDto.getYearOfBirth());
        newPerson.setPersonRole(PersonRole.USER);
        newPerson.setIsBanned(false);
        newPerson.setPassIsExpired(false);
        personRepository.saveAndFlush(newPerson);

        personDto.setLogin(null);
        personDto.setPassword(null);
    }

    @Transactional
    public void updateUser(PersonDto personDto) {
        Person person = personRepository.findPersonByLogin(personDto.getLogin()).orElse(null);
        Objects.requireNonNull(person).setFullName(personDto.getFullName());
        person.setLogin(personDto.getLogin());
        if(personDto.getNewPassword() != null){
            person.setPassword(passwordEncoder.encode(personDto.getNewPassword()));
            System.out.println("password changed");
        }
        person.setEmail(personDto.getEmail());
        person.setYearOfBirth(personDto.getYearOfBirth());
        personRepository.save(person);

    }

    public List<Person> getAllBannedUsers() {
        return personRepository.findAllByPersonRoleAndIsBanned(PersonRole.USER, true);
    }
}
