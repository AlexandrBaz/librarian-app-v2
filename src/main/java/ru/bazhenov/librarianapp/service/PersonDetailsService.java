package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.repositories.PersonRepository;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;
    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findUserByLogin(login);
        if(person.isEmpty()){
            System.out.println("User not found!");
            throw new UsernameNotFoundException("User not found!");
        }
        else {
            return User.builder()
                    .username(person.get().getLogin())
                    .password(person.get().getPassword())
                    .roles(person.get().getPersonRole().toString().replaceAll("ROLE_", ""))
                    .build();
        }
    }
}
