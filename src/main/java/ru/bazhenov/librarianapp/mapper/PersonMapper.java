package ru.bazhenov.librarianapp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.Person;
@Component
public class PersonMapper extends AbstractMapper<Person, PersonDto>{

    @Autowired
    public PersonMapper(){
        super(Person.class, PersonDto.class);
    }
}
