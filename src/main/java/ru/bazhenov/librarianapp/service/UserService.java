package ru.bazhenov.librarianapp.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;

import java.util.*;

@Service
public interface UserService {

    List<BookDto> getListEnableToTake(@NotNull PersonDto userDto);

    List<BookDto> searchBook(String key, PersonDto userDto);

    void addBookToUser(String login, long bookId);

    void returnBookFromPerson(String login, long bookId);

    PersonDto getProfileDto(String login);

    List<BookDto> getUserBooks(String login);

    void updateUser(ChangePersonDto personDto);

}
