package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.mapper.BookMapper;
import ru.bazhenov.librarianapp.mapper.PersonBookToBookDtoMapper;
import ru.bazhenov.librarianapp.mapper.PersonMapper;
import ru.bazhenov.librarianapp.models.Person;

import java.util.*;

@Service
@PreAuthorize("hasRole('ROLE_USER')")
public class UserService {
    private final BookService bookService;
    private final PersonServiceImpl personServiceImpl;
    private final PersonBookService personBookService;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;
    private final PersonBookToBookDtoMapper personBookToBookDtoMapper;

    @Autowired
    public UserService(BookService bookService, PersonServiceImpl personServiceImpl, PersonBookService personBookService, PersonMapper personMapper, BookMapper bookMapper, PersonBookToBookDtoMapper personBookToBookDtoMapper) {
        this.bookService = bookService;
        this.personServiceImpl = personServiceImpl;
        this.personBookService = personBookService;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
        this.personBookToBookDtoMapper = personBookToBookDtoMapper;
    }

    public PersonDto getUserDto(String login) {
        return personMapper.toDTO(personServiceImpl.getPersonByLogin(login));
    }
    public void updateUser(ChangePersonDto personDto) {
        personServiceImpl.updateUser(personDto);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')" )
    public List<BookDto> getUserBooks(String login) {
        return personServiceImpl.getPersonByLogin(login).getPersonBookList().stream()
                .map(personBookToBookDtoMapper::toDTO)
                .toList();
    }

    public List<BookDto> getListEnableToTake(PersonDto userDto) {
        return bookService.getAvailableBooksForUser(userDto.getId()).stream()
                .map(bookMapper::toDTO)
                .sorted(Comparator.comparing(BookDto::getName))
                .toList();
    }

    public List<BookDto> searchBook(String key, PersonDto userDto) {
        return getListEnableToTake(userDto).stream()
                .filter(bookDto -> bookDto.getName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT)))
                .toList();
    }

    public void addBookToUser(String login, long bookId) {
        personBookService.addBookToUser(personServiceImpl.getPersonByLogin(login), bookService.getBook(bookId));
        bookService.reduceBookCount(bookId);
    }

    public void returnBookFromPerson(String login, long bookId) {
        Person userEntity = personServiceImpl.getPersonByLogin(login);
        personBookService.returnBookFromPerson(userEntity, bookService.getBook(bookId));
        bookService.increaseBookCount(bookId);
    }
}
