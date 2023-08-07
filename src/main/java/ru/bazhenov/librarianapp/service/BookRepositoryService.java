package ru.bazhenov.librarianapp.service;

import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.models.Book;

import java.util.List;

@Service
public interface BookRepositoryService {

    Book getBook(long id);

    List<Book> getAllBooks(String sortBy);

    List<Book> getAvailableBooksForUser(long userId);

    void reduceBookCount(long bookId);

    void increaseBookCount(long bookId);

    void addBook(Book book);
}
