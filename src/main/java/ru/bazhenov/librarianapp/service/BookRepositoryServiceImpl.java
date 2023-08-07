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
public class BookRepositoryServiceImpl implements BookRepositoryService{

    private BookRepository bookRepository;

    @Override
    public Book getBook(long id) {
        return bookRepository.getReferenceById(id);
    }

    @Override
    public List<Book> getAllBooks(String sortBy) {
        return bookRepository.findAll(Sort.by(sortBy));
    }

    @Override
    public List<Book> getAvailableBooksForUser(long userId) {
        return bookRepository.findAvailableBookForUser(0L, userId);
    }

    @Override
    @Transactional
    public void reduceBookCount(long bookId) {
        Book book = getBook(bookId);
        book.setBooksCount(book.getBooksCount()-1);
        bookRepository.saveAndFlush(book);
    }

    @Override
    @Transactional
    public void increaseBookCount(long bookId) {
        Book book = getBook(bookId);
        book.setBooksCount(book.getBooksCount()+1);
        bookRepository.saveAndFlush(book);

    }

    @Override
    @Transactional
    public void addBook(Book book) {
        bookRepository.saveAndFlush(book);
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
}
