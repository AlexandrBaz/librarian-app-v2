package ru.bazhenov.librarianapp.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.mapper.PersonMapper;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.util.List;

@Service
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminService {

    private final PersonService personService;
    private final ManagerService managerService;
    private final UserService userService;
    private final PersonMapper personMapper;

    public AdminService(PersonService personService, ManagerService managerService, UserService userService, PersonMapper personMapper) {
        this.personService = personService;
        this.managerService = managerService;
        this.userService = userService;
        this.personMapper = personMapper;
    }

    public PersonDto getAdminDto(String login) {
        return personMapper.toDTO(personService.getPersonByLogin(login));
    }

    public List<PersonDto> getAllUsers() {
        return managerService.getAllUsers();
    }

    public List<PersonDto> getAllManagers() {
        return personService.getAllUser(PersonRole.MANAGER).stream()
                .map(personMapper::toDTO)
                .toList();
    }

    public List<BookDto> getAllBooks() {
        return managerService.getAllBooks();
    }

    public List<BookDto> getBooksNotReturned() {
        return managerService.getListOfDebtors();
    }

    public List<PersonDto> getBannedUsers() {
        return managerService.getListBannedUsers();
    }

    public List<PersonDto> getUsersList() {
        return managerService.getAllUsers();
    }

    public PersonDto getUserDto(long id) {
        return managerService.getUserDto(id);
    }

    public List<BookDto> getUserBooks(String login) {
        return userService.getUserBooks(login);
    }

    public void changePersonStatus(long id) {
        managerService.changePersonStatus(id);
    }

    public void registerNewUser(PersonDto personDto) {
        personService.registerNewUser(personDto, PersonRole.MANAGER);
    }

    public void updateUser(ChangePersonDto adminDto) {
        personService.updateUser(adminDto);
    }
}
