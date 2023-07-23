package ru.bazhenov.librarianapp.service;

import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;
import ru.bazhenov.librarianapp.repositories.PersonRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService{
    private PersonRepository personRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public Person getPersonById(long id){return personRepository.getReferenceById(id);}

    @Override
    public Person getPersonByLogin(String login){
        return personRepository.findPersonByLogin(login).orElse(null);
    }

    @Override
    public Boolean personByLoginIsPresent(String login) {
        return personRepository.findPersonByLogin(login).isPresent();
    }

    @Override
    public Boolean PersonByEmailIsPresent(String email) {
        return personRepository.findPersonByEmail(email).isPresent();
    }

    @Override
    public List<Person> getAllBannedUsers() {
        return personRepository.findAllByPersonRoleAndIsBanned(PersonRole.USER, true);
    }

    @Override
    public List<Person> getAllUser(PersonRole user) {
        return personRepository.findAllByPersonRole(user);
    }

    @Override
    @Transactional
    public void registerNewUser(PersonDto personDto, PersonRole personRole) {
        Person newPerson = new Person();
        newPerson.setFullName(personDto.getFullName());
        newPerson.setLogin(personDto.getLogin());
        newPerson.setPassword(passwordEncoder.encode(personDto.getPassword()));
        newPerson.setEmail(personDto.getEmail());
        newPerson.setYearOfBirth(personDto.getYearOfBirth());
        newPerson.setPersonRole(personRole);
        newPerson.setIsBanned(false);
        newPerson.setPassIsExpired(false);
        personRepository.saveAndFlush(newPerson);
    }

    @Override
    @Transactional
    public void updateUser(ChangePersonDto personDto) {
        Person person = personRepository.findPersonByLogin(personDto.getLogin()).orElse(null);
        Objects.requireNonNull(person).setFullName(personDto.getFullName());
        if(personDto.getNewPassword() != null && !personDto.getNewPassword().isBlank()){
            person.setPassword(passwordEncoder.encode(personDto.getNewPassword()));
        }
        person.setEmail(personDto.getEmail());
        person.setYearOfBirth(personDto.getYearOfBirth());
        personRepository.save(person);
    }

    @Override
    @Transactional
    public void changePersonStatus(Person person) {
        personRepository.saveAndFlush(person);
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
}
