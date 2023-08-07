package ru.bazhenov.librarianapp.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.Person;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl extends AbstractUserService implements UserService{

    public List<BookDto> getListEnableToTake(@NotNull PersonDto userDto) {
        return getBookRepositoryService().getAvailableBooksForUser(userDto.getId()).stream()
                .map(getBookMapper()::toDTO)
                .sorted(Comparator.comparing(BookDto::getName))
                .toList();
    }

    public List<BookDto> searchBook(String key, PersonDto userDto) {
        return getListEnableToTake(userDto).stream()
                .filter(bookDto -> bookDto.getName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT)))
                .toList();
    }

    public void addBookToUser(String login, long bookId) {
        getPersonBookRepositoryService().addBookToUser(getPersonRepositoryService().getPersonByLogin(login), getBookRepositoryService().getBook(bookId));
        getBookRepositoryService().reduceBookCount(bookId);
    }

    public void returnBookFromPerson(String login, long bookId) {
        Person userEntity = getPersonRepositoryService().getPersonByLogin(login);
        getPersonBookRepositoryService().returnBookFromPerson(userEntity, getBookRepositoryService().getBook(bookId));
        getBookRepositoryService().increaseBookCount(bookId);
    }
}
