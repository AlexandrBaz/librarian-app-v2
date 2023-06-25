package ru.bazhenov.librarianapp.util;

import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.BookDTO;
import ru.bazhenov.librarianapp.dto.PersonBookDTO;
import ru.bazhenov.librarianapp.dto.PersonDTO;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonBook;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OthersUtils {

    public Boolean isCyrillic(String text) {
        String cleanUpText = text.replaceAll("\\s|-|\\d+|", "").trim();
        for (int i = 0; i < cleanUpText.length(); i++) {
            if (!Character.UnicodeBlock.of(cleanUpText.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return false;
            }
        }
        return true;
    }

    public PersonDTO convertToPersonDTO(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getFullName(),
                person.getLogin(),
                person.getPassword(),
                person.getEmail(),
                person.getYearOfBirth(),
                convertToPersonBookDTO(person.getPersonBookList()),
                person.getPersonRole(),
                person.getIsBanned(),
                person.getPassIsExpired()
        );
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return new Person(
                personDTO.getId(),
                personDTO.getFullName(),
                personDTO.getLogin(),
                personDTO.getPassword(),
                personDTO.getEmail(),
                personDTO.getYearOfBirth(),
                convertToPersonBook(personDTO.getPersonBookList()),
                personDTO.getPersonRole(),
                personDTO.getIsBanned(),
                personDTO.getPassIsExpired()
        );
    }

    public BookDTO convertToBookDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getYear(),
                book.getBooksCount(),
                convertToPersonBookDTO(book.getPersonBookList())
        );
    }

    public Book convertToBook(BookDTO bookDTO) {
        return new Book(
                bookDTO.getId(),
                bookDTO.getName(),
                bookDTO.getAuthor(),
                bookDTO.getYear(),
                bookDTO.getBooksCount(),
                convertToPersonBook(bookDTO.getPersonBookList())
        );
    }

    public List<PersonBook> convertToPersonBook(List<PersonBookDTO> personBookDTOList) {
        return personBookDTOList.stream()
                .map(personBookDTO -> new PersonBook(
                        personBookDTO.getId(),
                        personBookDTO.getPersonBookDate(),
                        personBookDTO.getPersonBookDateExpiration(),
                        personBookDTO.getPersonBookIsExpired(),
                        convertToBook(personBookDTO.getBook()),
                        convertToPerson(personBookDTO.getPerson())
                ))
                .collect(Collectors.toList());
    }

    public List<PersonBookDTO> convertToPersonBookDTO(List<PersonBook> personBookList) {
        return personBookList.stream()
                .map(personBook -> new PersonBookDTO(
                        personBook.getId(),
                        personBook.getPersonBookDateExpiration(),
                        personBook.getPersonBookDate(),
                        personBook.getPersonBookIsExpired(),
                        convertToBookDTO(personBook.getBook()),
                        convertToPersonDTO(personBook.getPerson())))
                .collect(Collectors.toList());
    }

    public List<BookDTO> convertToBookDTO(List<Book> books) {
        return books.stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getYear(),
                        book.getBooksCount(),
                        convertToPersonBookDTO(book.getPersonBookList())
                ))
                .collect(Collectors.toList());
    }

    public List<Book> convertToBook(List<BookDTO> bookDTOList){
        return bookDTOList.stream()
                .map(bookDTO -> new Book(
                        bookDTO.getId(),
                        bookDTO.getName(),
                        bookDTO.getAuthor(),
                        bookDTO.getYear(),
                        bookDTO.getBooksCount(),
                        convertToPersonBook(bookDTO.getPersonBookList())
                ))
                .collect(Collectors.toList());

    }
}
