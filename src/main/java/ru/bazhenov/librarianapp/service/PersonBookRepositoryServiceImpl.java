package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonBook;
import ru.bazhenov.librarianapp.repositories.PersonBookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PersonBookRepositoryServiceImpl implements PersonBookRepositoryService{
    private PersonBookRepository personBookRepository;

    @Override
    public List<PersonBook> getAllPersonBook() {
        return personBookRepository.findAll();
    }

    @Override
    @Transactional
    public void addBookToUser(Person person, Book book) {
        PersonBook personBook = new PersonBook();
        personBook.setPersonBookDate(LocalDate.now());
        personBook.setPerson(person);
        personBook.setBook(book);
        personBookRepository.saveAndFlush(personBook);
    }

    @Override
    @Transactional
    public void returnBookFromPerson(Person person, Book book) {
        PersonBook personBook = personBookRepository.findFirstByPersonAndBook(person, book).orElse(null);
        personBookRepository.delete(Objects.requireNonNull(personBook));
    }

    @Autowired
    public void setPersonBookRepository(PersonBookRepository personBookRepository){
        this.personBookRepository = personBookRepository;
    }
}
