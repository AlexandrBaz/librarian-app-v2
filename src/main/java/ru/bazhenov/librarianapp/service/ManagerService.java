package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.mapper.BookMapper;
import ru.bazhenov.librarianapp.mapper.PersonBookToBookDtoMapper;
import ru.bazhenov.librarianapp.mapper.PersonMapper;
import ru.bazhenov.librarianapp.models.Person;

import java.util.List;
import java.util.Locale;

@Service
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class ManagerService {
    private final PersonService personService;
    private final PersonBookService personBookService;
    private final BookService bookService;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;
    private final PersonBookToBookDtoMapper personBookToBookDtoMapper;

    @Autowired
    public ManagerService(PersonMapper personMapper, PersonService personService, PersonBookService personBookService, BookService bookService, BookMapper bookMapper, PersonBookToBookDtoMapper personBookToBookDtoMapper) {
        this.personMapper = personMapper;
        this.personService = personService;
        this.personBookService = personBookService;
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.personBookToBookDtoMapper = personBookToBookDtoMapper;
    }

    public PersonDto getManagerDto(String login) {
        return personMapper.toDTO(personService.getPersonByLogin(login));
    }

    public List<BookDto> getListOfDebtors() {
        return personBookService.getAllPersonBook().stream()
                .map(personBookToBookDtoMapper::toDTO)
                .filter(BookDto::getBookReturnIsExpired)
                .toList();
    }

    public List<PersonDto> getListBannedUsers() {
        return personService.getAllBannedUsers().stream()
                .map(personMapper::toDTO)
                .toList();
    }

    public PersonDto getUserDto(long id) {
        return personMapper.toDTO(personService.getPerson(id));
    }

    public void changePersonStatus(long id) {
        Person person = personService.getPerson(id);
        if (person.getIsBanned()) {
            person.setIsBanned(Boolean.FALSE);
        } else {
            person.setIsBanned(Boolean.TRUE);
        }
        personService.changePersonStatus(person);
    }

    public List<BookDto> searchBook(String key) {
        return bookService.getAllBooks("name").stream()
                .map(bookMapper::toDTO)
                .filter(bookDto -> bookDto.getName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT)))
                .toList();
    }

    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks("name").stream()
                .map(bookMapper::toDTO)
                .toList();
    }
}
