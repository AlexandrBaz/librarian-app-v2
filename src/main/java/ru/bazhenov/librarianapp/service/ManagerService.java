package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.mapper.PersonBookToBookDtoMapper;
import ru.bazhenov.librarianapp.mapper.PersonMapper;

import java.util.List;

@Service
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class ManagerService {

    private final PersonMapper personMapper;
    private final PersonService personService;
    private final PersonBookService personBookService;
    private final PersonBookToBookDtoMapper personBookToBookDtoMapper;

    @Autowired
    public ManagerService(PersonMapper personMapper, PersonService personService, PersonBookService personBookService,PersonBookToBookDtoMapper personBookToBookDtoMapper) {
        this.personMapper = personMapper;
        this.personService = personService;
        this.personBookService = personBookService;
        this.personBookToBookDtoMapper = personBookToBookDtoMapper;
    }

    public PersonDto getManagerDto(String login) {
        return personMapper.toDTO(personService.getPersonByLogin(login));
    }

    public List<BookDto> getListOfDebtors(){
        return personBookService.getAllPersonBook().stream()
                .map(personBookToBookDtoMapper::toDTO)
                .filter(BookDto::getBookReturnIsExpired)
                .toList();
    }

    public List<PersonDto> getListBannedUsers() {
        return personService.getAllBannedUsers().stream()
                .map(personMapper::toDTO)
                .toList();
    }

    public PersonDto getUserDto(long id) {
        return personMapper.toDTO(personService.getPerson(id));
    }
}