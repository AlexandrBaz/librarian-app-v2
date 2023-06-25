package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDTO;
import ru.bazhenov.librarianapp.dto.PersonBookDTO;
import ru.bazhenov.librarianapp.dto.PersonDTO;
import ru.bazhenov.librarianapp.util.OthersUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final BookService bookService;
    private final OthersUtils othersUtils;
    private final static int bookDaysToExpired = 10;
    @Autowired
    public UserService(BookService bookService, OthersUtils othersUtils) {
        this.bookService = bookService;
        this.othersUtils = othersUtils;
    }
    public PersonDTO ensureUserProfileDTO(PersonDTO personDTO) {
        List<PersonBookDTO> personBookListDTO = personDTO.getPersonBookList();
        if (!personBookListDTO.isEmpty()) {
            personBookListDTO.forEach(bookTaken -> {
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

    public List<BookDTO> getListEnableToTake(PersonDTO personDTO) {
        List<BookDTO> bookTakenList = personDTO.getPersonBookList().stream()
                .map(PersonBookDTO::getBook)
                .toList();
        return othersUtils.convertToBookDTO(bookService.getBooks()).stream()
                .filter(book -> !bookTakenList.contains(book))
                .toList();
    }

//    public List<Book> getListEnableToTake(PersonDTO userProfileDTO, List<Book> books) {
//        List<BookDTO> bookTakenList = userProfileDTO.getPersonBookList().stream()
//                .map(PersonBookDTO::getBook)
//                .toList();
//        return books.stream()
//                .filter(book -> !bookTakenList.contains(book))
//                .toList();
//    }

    public Page<BookDTO> findPaginated(Pageable pageable, PersonDTO personDTO) {
        List<BookDTO> books = getListEnableToTake(personDTO);
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

        Page<BookDTO> bookPage
                = new PageImpl<BookDTO>(list, PageRequest.of(currentPage, pageSize), books.size());

        return bookPage;
    }

}
