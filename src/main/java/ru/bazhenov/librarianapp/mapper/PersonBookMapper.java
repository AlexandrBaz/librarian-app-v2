package ru.bazhenov.librarianapp.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.PersonBookDto;
import ru.bazhenov.librarianapp.models.PersonBook;
import ru.bazhenov.librarianapp.service.BookService;
import ru.bazhenov.librarianapp.service.PersonServiceImpl;

import java.util.Objects;

@Component
public class PersonBookMapper extends AbstractMapper<PersonBook, PersonBookDto>{
    private final ModelMapper modelMapper;
    private final PersonServiceImpl personServiceImpl;
    private final BookService bookService;

    @Autowired
    public PersonBookMapper(ModelMapper modelMapper, PersonServiceImpl personServiceImpl, BookService bookService) {
        super(PersonBook.class, PersonBookDto.class);
        this.modelMapper = modelMapper;
        this.personServiceImpl = personServiceImpl;
        this.bookService = bookService;
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
        destination.setBook(bookService.getBook(source.getBookId()));
    }
    void mapPersonField(PersonBookDto source, PersonBook destination){
        destination.setPerson(personServiceImpl.getPersonById(source.getPersonId()));
    }
}