package ru.bazhenov.librarianapp.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.PersonBookDto;
import ru.bazhenov.librarianapp.models.PersonBook;
import ru.bazhenov.librarianapp.service.BookRepositoryService;
import ru.bazhenov.librarianapp.service.PersonRepositoryService;

import java.util.Objects;

@Component
public class PersonBookMapper extends AbstractMapper<PersonBook, PersonBookDto>{
    private final ModelMapper modelMapper;
    private final PersonRepositoryService personRepositoryService;
    private final BookRepositoryService bookRepositoryService;

    @Autowired
    public PersonBookMapper(ModelMapper modelMapper, PersonRepositoryService personRepositoryService, BookRepositoryService bookRepositoryService) {
        super(PersonBook.class, PersonBookDto.class);
        this.modelMapper = modelMapper;
        this.personRepositoryService = personRepositoryService;
        this.bookRepositoryService = bookRepositoryService;
    }
    @PostConstruct
    public void setupMapper(){
        modelMapper.createTypeMap(PersonBook.class, PersonBookDto.class)
                .addMappings(mapper -> mapper.skip(PersonBookDto::setBookId)).setPostConverter(toDtoConverter())
                .addMappings(mapper -> mapper.skip(PersonBookDto::setPersonId)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(PersonBookDto.class, PersonBook.class)
                .addMappings(mapper -> mapper.skip(PersonBook::setBook)).setPostConverter(toEntityConverter())
                .addMappings(mapper -> mapper.skip(PersonBook::setPerson)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapBookField(PersonBook source, PersonBookDto destination){
        destination.setBookId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getBook().getId());
    }
    @Override
    void mapPersonField(PersonBook source, PersonBookDto destination){
        destination.setPersonId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getPerson().getId());
    }

    @Override
    void mapBookField(PersonBookDto source, PersonBook destination){
        destination.setBook(bookRepositoryService.getBook(source.getBookId()));
    }
    void mapPersonField(PersonBookDto source, PersonBook destination){
        destination.setPerson(personRepositoryService.getPersonById(source.getPersonId()));
    }
}