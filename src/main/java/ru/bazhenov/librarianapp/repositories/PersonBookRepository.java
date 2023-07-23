package ru.bazhenov.librarianapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.PersonBook;
import ru.bazhenov.librarianapp.models.Person;

import java.util.Optional;
@Repository
public interface PersonBookRepository extends JpaRepository<PersonBook, Long> {
    Optional<PersonBook> findFirstByPersonAndBook (Person person, Book book);
}
