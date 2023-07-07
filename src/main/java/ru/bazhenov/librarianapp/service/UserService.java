package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.mapper.BookMapper;
import ru.bazhenov.librarianapp.mapper.PersonBookToBookDtoMapper;
import ru.bazhenov.librarianapp.mapper.PersonMapper;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@PreAuthorize("hasRole('ROLE_USER')")
public class UserService {
    private final BookService bookService;
    private final PersonService personService;
    private final PersonBookService personBookService;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;
    private final PersonBookToBookDtoMapper personBookToBookDtoMapper;

    @Autowired
    public UserService(BookService bookService, PersonService personService, PersonBookService personBookService, PersonMapper personMapper, BookMapper bookMapper, PersonBookToBookDtoMapper personBookToBookDtoMapper) {
        this.bookService = bookService;
        this.personService = personService;
        this.personBookService = personBookService;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
        this.personBookToBookDtoMapper = personBookToBookDtoMapper;
    }

    public PersonDto getUserDto(String login) {
        return personMapper.toDTO(personService.getPersonByLogin(login));
    }

    public List<BookDto> getUserBooks(String login) {
        return personService.getPersonByLogin(login).getPersonBookList().stream()
                .map(personBookToBookDtoMapper::toDTO)
                .toList();
    }

    public List<BookDto> getListEnableToTake(PersonDto userDto, String sortBy) {
        List<Book> personBookList = new ArrayList<>();
        userDto.getPersonBookList().forEach(personBookDTO -> personBookList.add(bookService.getBook(personBookDTO.getBookId())));
        return bookService.getBooks(sortBy).stream()
                .filter(book -> !personBookList.contains(book))
                .map(bookMapper::toDTO)
                .toList();
    }

    public Page<BookDto> findPaginated(Pageable pageable, PersonDto userDto, String sortBy) {
        List<BookDto> books = getListEnableToTake(userDto, sortBy);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<BookDto> list;
        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), books.size());
    }

    public List<BookDto> searchBook(String key, PersonDto userDto) {
        List<BookDto> books = getListEnableToTake(userDto, "name");
        return books.stream()
                .filter(bookDto -> bookDto.getName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT)))
                .toList();
    }

    public void addBookToUser(String login, long bookId) {
        personBookService.addBookToUser(personService.getPersonByLogin(login), bookService.getBook(bookId));
        bookService.reduceBookCount(bookId);
    }

    public void returnBookFromPerson(String login, long bookId) {
        Person userEntity = personService.getPersonByLogin(login);
        personBookService.returnBookFromPerson(userEntity, bookService.getBook(bookId));
        bookService.increaseBookCount(bookId);
    }

    public void updateUser(PersonDto personDto) {
        personService.updateUser(personDto);
    }
}
