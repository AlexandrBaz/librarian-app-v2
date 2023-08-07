package ru.bazhenov.librarianapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.util.List;
import java.util.Optional;
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findPersonByLogin(String userName);
    Optional<Person> findPersonByEmail(String email);
    List<Person> findAllByPersonRoleAndIsBanned(PersonRole personRole, Boolean isBanned);
    List<Person> findAllByPersonRole(PersonRole personRole);
}
