package ru.bazhenov.librarianapp.service;

import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;

import java.util.List;

@Service
public interface AdminService{

    List<PersonDto> getAllManagers();

    void registerNewUser(PersonDto personDto);

    PersonDto getProfileDto(String login);

    List<BookDto> getUserBooks(String login);

    PersonDto getUserDto(long id);

    void changePersonStatus(long id);

    void updateUser(ChangePersonDto personDto);

    List<BookDto> getListOfDebtors();

    List<PersonDto> getListBannedUsers();

    List<BookDto> getAllBooks();

    List<PersonDto> getAllUsers();
}
