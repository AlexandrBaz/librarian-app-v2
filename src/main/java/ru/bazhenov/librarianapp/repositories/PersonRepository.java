package ru.bazhenov.librarianapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bazhenov.librarianapp.models.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findUserByLogin(String userName);
    Optional<Person> findUserByEmail(String email);
}
