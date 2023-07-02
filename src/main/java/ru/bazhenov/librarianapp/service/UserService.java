package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.AbstractDTO;
import ru.bazhenov.librarianapp.dto.BookDTO;
import ru.bazhenov.librarianapp.dto.PersonBookDTO;
import ru.bazhenov.librarianapp.dto.PersonDTO;
import ru.bazhenov.librarianapp.mapper.BookMapper;
import ru.bazhenov.librarianapp.mapper.PersonBookMapper;
import ru.bazhenov.librarianapp.mapper.PersonMapper;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonBook;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final BookService bookService;
    private final PersonService personService;
    private final PersonBookMapper personBookMapper;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;
    private final static int bookDaysToExpired = 10;
    @Autowired
    public UserService(BookService bookService, PersonService personService, PersonBookMapper personBookMapper, PersonMapper personMapper, BookMapper bookMapper) {
        this.bookService = bookService;
        this.personService = personService;
        this.personBookMapper = personBookMapper;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
    }
    public PersonDTO getUserDto(String login){
        return personMapper.toDTO(personService.getPersonByLogin(login));
    }

    public List<BookDTO> getUserBooks(PersonDTO userDto) {
        return userDto.getPersonBookList().stream()
                .map(personBookDTO -> bookService.getBook(personBookDTO.getBookId()))
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());

    }
    public PersonDTO ensurePerson(String login) {
        PersonDTO personDTO = personMapper.toDTO(personService.getPersonByLogin(login));
        List<PersonBookDTO> personBookList = personDTO.getPersonBookList();
        if (!personBookList.isEmpty()) {
            personBookList.forEach(bookTaken -> {
                bookTaken.setPersonBookDateExpirationDTO(bookDaysToExpired);
                bookTaken.setPersonBookIsExpired(bookIsExpired(bookTaken.getPersonBookDate()));
            });
        }
        return personDTO;
    }

    public Boolean bookIsExpired(Date dateTaken) {
        LocalDate bookTakenDate = dateTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(bookTakenDate, LocalDate.now()) > bookDaysToExpired;
    }

    public List<BookDTO> getListEnableToTake(Person person, String sortBy) {
        List<Book> personBookList = new ArrayList<>();
        person.getPersonBookList().forEach(personBookDTO -> {
            personBookList.add(bookService.getBook(personBookDTO.getBook().getId()));
        });
        return bookService.getBooks(sortBy).stream()
                .filter(book -> !personBookList.contains(book))
                .map(bookMapper::toDTO)
                .toList();
    }
    public Page<BookDTO> findPaginated(Pageable pageable, Person person, String sortBy) {
        List<BookDTO> books = getListEnableToTake(person, sortBy);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<BookDTO> list;

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }



        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), books.size());
    }


}
