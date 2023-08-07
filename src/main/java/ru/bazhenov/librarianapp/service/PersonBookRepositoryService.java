package ru.bazhenov.librarianapp.service;

import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonBook;

import java.util.List;

@Service
public interface PersonBookRepositoryService {

    List<PersonBook> getAllPersonBook();

    void addBookToUser(Person person, Book book);

    void returnBookFromPerson(Person person, Book book);
}
