package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.PersonDTO;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonBook;
import ru.bazhenov.librarianapp.util.OthersUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final BookService bookService;
    private final PersonService personService;
    private final static int bookDaysToExpired = 10;
    @Autowired
    public UserService(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }
    public Person ensurePerson(String login) {
        Person person = personService.getPersonByLogin(login);
        List<PersonBook> personBookList = person.getPersonBookList();
        if (!personBookList.isEmpty()) {
            personBookList.forEach(bookTaken -> {
                bookTaken.setPersonBookDateExpiration(bookDaysToExpired);
                bookTaken.setPersonBookIsExpired(bookIsExpired(bookTaken.getPersonBookDate()));
            });
        }
        return person;
    }

    public Boolean bookIsExpired(Date dateTaken) {
        LocalDate bookTakenDate = dateTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(bookTakenDate, LocalDate.now()) > bookDaysToExpired;
    }

    public List<Book> getListEnableToTake(Person person) {
        List<Book> personBookList = new ArrayList<>();
        person.getPersonBookList().forEach(personBookDTO -> {
            personBookList.add(bookService.getBook(personBookDTO.getId()));
        });
        return bookService.getBooks().stream()
                .filter(book -> !personBookList.contains(book))
                .toList();
    }
    public Page<Book> findPaginated(Pageable pageable, Person person) {
        List<Book> books = getListEnableToTake(person);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        return new PageImpl<Book>(list, PageRequest.of(currentPage, pageSize), books.size());
    }

}
