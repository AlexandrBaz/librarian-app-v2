package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.repositories.BookRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBook(long id){
       return bookRepository.getReferenceById(id);
    }

    public List<Book> getBooks(String sortBy){
        return bookRepository.findAllByBooksCountGreaterThan(0L, Sort.by(sortBy));
    }

    @Transactional
    public void reduceBookCount(long bookId){
        Book book = getBook(bookId);
        book.setBooksCount(book.getBooksCount()-1);
        bookRepository.saveAndFlush(book);
    }

    @Transactional
    public void increaseBookCount(long bookId) {
        Book book = getBook(bookId);
        book.setBooksCount(book.getBooksCount()+1);
        bookRepository.saveAndFlush(book);
    }
}