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
    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public PersonBookService(PersonBookRepository personBookRepository, PersonService personService, BookService bookService) {
        this.personBookRepository = personBookRepository;
        this.personService = personService;
        this.bookService = bookService;
    }

    @Transactional
    public void addBookToUser(long personId, long bookId) {
        System.out.println(personId + " personId");
        System.out.println(bookId + " bookId");
        PersonBook personBook = new PersonBook();
        personBook.setPersonBookDate(new Date());
        personBook.setPerson(personService.getPerson(personId));
        personBook.setBook(bookService.getBook(bookId));

        personBookRepository.saveAndFlush(personBook);
        bookService.reduceBookCount(bookId);
    }

    @Transactional
    public void returnBookFromPerson(Person person, Book book) {

        PersonBook personBook = personBookRepository.findFirstByPersonAndBook(person,book).orElse(null);
        personBookRepository.delete(Objects.requireNonNull(personBook));
        bookService.increaseBookCount(book.getId());
    }
}
