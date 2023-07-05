package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.PersonBook;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.repositories.PersonBookRepository;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PersonBookService {

    private final PersonBookRepository personBookRepository;

    @Autowired
    public PersonBookService(PersonBookRepository personBookRepository) {
        this.personBookRepository = personBookRepository;
    }

    @Transactional
    public void addBookToUser(Person person, Book book) {
        PersonBook personBook = new PersonBook();
        personBook.setPersonBookDate(new Date());
        personBook.setPerson(person);
        personBook.setBook(book);
        personBookRepository.saveAndFlush(personBook);
    }

    @Transactional
    public void returnBookFromPerson(Person person, Book book) {
        PersonBook personBook = personBookRepository.findFirstByPersonAndBook(person, book).orElse(null);
        personBookRepository.delete(Objects.requireNonNull(personBook));
    }
}
