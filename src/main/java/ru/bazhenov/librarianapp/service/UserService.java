package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.mapper.BookMapper;
import ru.bazhenov.librarianapp.mapper.PersonMapper;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserService {
    private final BookService bookService;
    private final PersonService personService;
    private final PersonBookService personBookService;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;
    @Value("${bookDaysToExpired}")
    static int bookDaysToExpired;

    @Autowired
    public UserService(BookService bookService, PersonService personService, PersonBookService personBookService, PersonMapper personMapper, BookMapper bookMapper) {
        this.bookService = bookService;
        this.personService = personService;
        this.personBookService = personBookService;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
    }

    public PersonDto getUserDto(String login) {
        return personMapper.toDTO(personService.getPersonByLogin(login));
    }

    public List<BookDto> getUserBooks(PersonDto userDto) {
        List<BookDto> bookDtoList = new ArrayList<>();
        userDto.getPersonBookList().forEach(personBookDto -> {
            BookDto bookDto = bookMapper.toDTO(bookService.getBook(personBookDto.getBookId()));
            bookDto.setBookDateTaken(personBookDto.getPersonBookDate());
            bookDto.setBookDateExpiration(bookDaysToExpired);
            bookDto.setBookReturnIsExpired(bookIsExpired(personBookDto.getPersonBookDate()));
            bookDtoList.add(bookDto);
        });
        return bookDtoList;
    }

    private Boolean bookIsExpired(Date dateTaken) {
        LocalDate bookTakenDate = dateTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(bookTakenDate, LocalDate.now()) > bookDaysToExpired;
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
