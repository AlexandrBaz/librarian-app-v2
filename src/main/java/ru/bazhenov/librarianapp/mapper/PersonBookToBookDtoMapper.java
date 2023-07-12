package ru.bazhenov.librarianapp.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.models.PersonBook;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class PersonBookToBookDtoMapper extends AbstractMapper<PersonBook, BookDto>{

    private final ModelMapper modelMapper;
    @Value(value = "${book.bookDaysToExpired}")
    private long bookDaysToExpired;
    @Autowired
    public PersonBookToBookDtoMapper(ModelMapper modelMapper) {
        super(PersonBook.class, BookDto.class);
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper(){
        modelMapper.createTypeMap(PersonBook.class, BookDto.class)
                .addMappings(mapper -> mapper.map(source -> source.getBook().getId(), BookDto::setId))
                .addMappings(mapper -> mapper.map(source -> source.getBook().getName(), BookDto::setName))
                .addMappings(mapper -> mapper.map(source -> source.getBook().getAuthor(), BookDto::setAuthor))
                .addMappings(mapper -> mapper.map(source -> source.getBook().getYear(), BookDto::setYear))
                .addMappings(mapper -> mapper.map(PersonBook::getPersonBookDate, BookDto::setBookDateTaken))
                .addMappings(mapper -> mapper.skip(BookDto::setBookDateExpiration)).setPostConverter(toDtoConverter())
                .addMappings(mapper -> mapper.skip(BookDto::setBookReturnIsExpired)).setPostConverter(toDtoConverter())
                .addMappings(mapper -> mapper.skip(BookDto::setTotalDaysExpire)).setPostConverter(toDtoConverter())
                .addMappings(mapper -> mapper.map(source -> source.getPerson().getFullName(), BookDto::setUserName))
                .addMappings(mapper -> mapper.map(source -> source.getPerson().getId(), BookDto::setUserId));
    }

    @Override
    void mapBookDateExpiration(PersonBook source, BookDto destination) {
        destination.setBookDateExpiration(Objects.isNull(source) || Objects.isNull(source.getPersonBookDate()) ? null :
                source.getPersonBookDate().plusDays(bookDaysToExpired));
    }
    @Override
    void mapBookReturnIsExpired(PersonBook source, BookDto destination){
        destination.setBookReturnIsExpired(Objects.isNull(source) || Objects.isNull(source.getPersonBookDate()) ? null :
                source.getPersonBookDate().plusDays(bookDaysToExpired).isBefore(LocalDate.now()));
    }
    @Override
    void mapTotalDaysExpire(PersonBook source, BookDto destination) {
        destination.setTotalDaysExpire(Objects.isNull(source) || Objects.isNull(source.getPersonBookDate()) ? null :
                ChronoUnit.DAYS.between(source.getPersonBookDate().plusDays(bookDaysToExpired), LocalDate.now()));
    }
}