package ru.bazhenov.librarianapp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.models.Book;
@Component
public class BookMapper extends AbstractMapper<Book, BookDto>{
    @Autowired
    public BookMapper(){
        super(Book.class, BookDto.class);
    }
}
