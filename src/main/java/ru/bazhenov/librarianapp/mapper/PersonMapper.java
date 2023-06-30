package ru.bazhenov.librarianapp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.PersonDTO;
import ru.bazhenov.librarianapp.models.Person;
@Component
public class PersonMapper extends AbstractMapper<Person, PersonDTO>{

    @Autowired
    public PersonMapper(){
        super(Person.class, PersonDTO.class);
    }
}
