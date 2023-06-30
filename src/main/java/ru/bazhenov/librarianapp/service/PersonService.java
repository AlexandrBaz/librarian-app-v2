package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
    public void registerNewUser(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setPersonRole(PersonRole.USER);
        person.setIsBanned(false);
        person.setPassIsExpired(false);
        personRepository.saveAndFlush(person);
    }
}
