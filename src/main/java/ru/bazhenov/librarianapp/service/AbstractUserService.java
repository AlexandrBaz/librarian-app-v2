package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.mapper.BookMapper;
import ru.bazhenov.librarianapp.mapper.PersonBookToBookDtoMapper;
import ru.bazhenov.librarianapp.mapper.PersonMapper;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.util.List;

@Service
public abstract class AbstractUserService {
    private PersonRepositoryService personRepositoryService;
    private BookRepositoryService bookRepositoryService;
    private PersonBookRepositoryService personBookRepositoryService;
    private PersonBookToBookDtoMapper personBookToBookDtoMapper;
    private PersonMapper personMapper;
    private BookMapper bookMapper;

    public PersonDto getProfileDto(String login){
        return personMapper.toDTO(personRepositoryService.getPersonByLogin(login));
    }

    public List<BookDto> getUserBooks(String login) {
        return personRepositoryService.getPersonByLogin(login).getPersonBookList().stream()
                .map(personBookToBookDtoMapper::toDTO)
                .toList();
    }

    public PersonDto getUserDto(long id) {
        return personMapper.toDTO(personRepositoryService.getPersonById(id));
    }

    public void changePersonStatus(long id) {
        Person person = personRepositoryService.getPersonById(id);
        if (person.getIsBanned()) {
            person.setIsBanned(Boolean.FALSE);
        } else {
            person.setIsBanned(Boolean.TRUE);
        }
        personRepositoryService.changePersonStatus(person);
    }

    public void updateUser(ChangePersonDto personDto) {
        personRepositoryService.updateUser(personDto);
    }

    public List<BookDto> getListOfDebtors() {
        return personBookRepositoryService.getAllPersonBook().stream()
                .map(personBookToBookDtoMapper::toDTO)
                .filter(BookDto::getBookReturnIsExpired)
                .toList();
    }

    public List<PersonDto> getListBannedUsers() {
        return personRepositoryService.getAllBannedUsers().stream()
                .map(personMapper::toDTO)
                .toList();
    }

    public List<BookDto> getAllBooks() {
        return bookRepositoryService.getAllBooks("name").stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    public List<PersonDto> getAllUsers() {
        return personRepositoryService.getAllUser(PersonRole.USER).stream()
                .map(personMapper::toDTO)
                .toList();
    }
    public PersonRepositoryService getPersonRepositoryService(){
        return this.personRepositoryService;
    }

    public BookRepositoryService getBookRepositoryService(){
        return this.bookRepositoryService;
    }

    public PersonBookRepositoryService getPersonBookRepositoryService(){
        return this.personBookRepositoryService;
    }

    public PersonMapper getPersonMapper() {
        return this.personMapper;
    }

    public BookMapper getBookMapper(){
        return this.bookMapper;
    }

    @Autowired
    public void setPersonRepositoryService(PersonRepositoryService personRepositoryService){
        this.personRepositoryService = personRepositoryService;
    }
    @Autowired
    public void setBookRepositoryService(BookRepositoryService bookRepositoryService){
        this.bookRepositoryService = bookRepositoryService;
    }
    @Autowired
    public void setPersonBookRepositoryService(PersonBookRepositoryService personBookRepositoryService){
        this.personBookRepositoryService = personBookRepositoryService;
    }
    @Autowired
    public void setPersonBookToBookDtoMapper(PersonBookToBookDtoMapper personBookToBookDtoMapper){
        this.personBookToBookDtoMapper = personBookToBookDtoMapper;
    }
    @Autowired
    public void setPersonMapper(PersonMapper personMapper){
        this.personMapper = personMapper;
    }
    @Autowired
    public void setBookMapper(BookMapper bookMapper){
        this.bookMapper = bookMapper;
    }
}