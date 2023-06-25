package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;
import ru.bazhenov.librarianapp.repositories.PersonRepository;

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

    public Person getUserProfileById(long id) {
        return personRepository.getReferenceById(id);
    }

    public Person getUserProfileByLogin(String login){
        return personRepository.findUserByLogin(login).orElse(null);
    }

    public Boolean userByLoginIsPresent(String login) {
        return personRepository.findUserByLogin(login).isPresent();
    }

    public Boolean getUserByEmail(String email) {
        return personRepository.findUserByEmail(email).isPresent();
    }


    @Transactional
    public void registerNewUser(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setPersonRole(PersonRole.USER);
        person.setIsBanned(false);
        person.setPassIsExpired(false);
        personRepository.saveAndFlush(person);
    }
}
