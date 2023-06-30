package ru.bazhenov.librarianapp.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.PersonBookDTO;
import ru.bazhenov.librarianapp.models.PersonBook;
import ru.bazhenov.librarianapp.service.BookService;
import ru.bazhenov.librarianapp.service.PersonService;

import java.util.Objects;

@Component
public class PersonBookMapper extends AbstractMapper<PersonBook, PersonBookDTO>{
    private final ModelMapper modelMapper;
    private final PersonService personService;
    private final BookService bookService;
    @Autowired
    public PersonBookMapper(ModelMapper modelMapper, PersonService personService, BookService bookService) {
        super(PersonBook.class, PersonBookDTO.class);
        this.modelMapper = modelMapper;
        this.personService = personService;
        this.bookService = bookService;
    }
    @PostConstruct
    public void setupMapper(){
        modelMapper.createTypeMap(PersonBook.class, PersonBookDTO.class)
                .addMappings(mapper -> mapper.skip(PersonBookDTO::setBookId)).setPostConverter(toDtoConverter())
                .addMappings(mapper -> mapper.skip(PersonBookDTO::setPersonId)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(PersonBookDTO.class, PersonBook.class)
                .addMappings(mapper -> mapper.skip(PersonBook::setBook)).setPostConverter(toEntityConverter())
                .addMappings(mapper -> mapper.skip(PersonBook::setPerson)).setPostConverter(toEntityConverter());
    }
    @Override
    void mapBookField(PersonBook source, PersonBookDTO destination){
        destination.setBookId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getBook().getId());
    }
    @Override
    void mapPersonField(PersonBook source, PersonBookDTO destination){
        destination.setPersonId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getPerson().getId());
    }

    @Override
    void mapBookField(PersonBookDTO source, PersonBook destination){
        destination.setBook(bookService.getBook(source.getId()));
    }
    void mapPersonField(PersonBookDTO source, PersonBook destination){
        destination.setPerson(personService.getPerson(source.getId()));
    }
}