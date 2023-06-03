package ru.bazhenov.librarianapp.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.BookAndReaders;
import ru.bazhenov.librarianapp.models.PaginationAndSort;
import ru.bazhenov.librarianapp.models.Reader;
import ru.bazhenov.librarianapp.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final ReaderService readerService;
    @Autowired
    public BookService(BookRepository bookRepository, @Lazy ReaderService readerService) {
        this.bookRepository = bookRepository;
        this.readerService = readerService;
    }
    public List<Book> index() {
        return bookRepository.findAll(Sort.by("author"));
    }
    public PaginationAndSort index(int offset, int limit){
        PaginationAndSort paginationAndSort = new PaginationAndSort();
        paginationAndSort.setCurrentPage(offset+1);
        paginationAndSort.setNumberOfPage(bookRepository.findAll(PageRequest.of(offset,limit)).getTotalPages());
        paginationAndSort.setBookPage(bookRepository.findAll(PageRequest.of(offset,limit,Sort.by("id"))));
        return paginationAndSort;
    }
    public PaginationAndSort paginationAndSort(int offset, int limit){
        PaginationAndSort paginationAndSort = new PaginationAndSort();
        paginationAndSort.setCurrentPage(offset+1);
        paginationAndSort.setNumberOfPage(bookRepository.findAll(PageRequest.of(offset,limit)).getTotalPages());
        paginationAndSort.setBookPage(bookRepository.findAll(PageRequest.of(offset,limit,Sort.by("year"))));
        return paginationAndSort;
    }
    public List<Book> indexSortByYear(){
        return bookRepository.findAll(Sort.by("year"));
    }
    public List<Book> search(String search){
        return bookRepository.findAllByNameContainsIgnoreCase(search);
    }
    @Transactional
    public void addNewBook(Book book) {
        bookRepository.saveAndFlush(book);
    }

    public Book getBookById(int id){
        return bookRepository.findById(id).orElse(null);
    }

    public BookAndReaders getBookList(int id){
        BookAndReaders bookAndReaders = new BookAndReaders();
        Book book = bookRepository.findById(id).orElse(null);
        bookAndReaders.setBook(book);
        bookAndReaders.setReaderList(readerService.index());
        bookAndReaders.setThisBookReader(Objects.requireNonNull(bookRepository.findById(id).orElse(null)).getReader());
        bookAndReaders.setBookIsTaken(Objects.requireNonNull(book).getReader() !=null);
        return bookAndReaders;
    }

    @Transactional
    public void updateBook(int id, @NotNull Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.saveAndFlush(updatedBook);
    }
    @Transactional
    public void resetReader(int id) {
        Book book = bookRepository.findById(id).orElse(null);
        Objects.requireNonNull(book).setReader(null);
        book.setBookTakenDate(null);
        bookRepository.saveAndFlush(book);
    }
    @Transactional
    public void setBookReader(int readerId, int bookId) {
        Reader reader = readerService.getByIdReader(readerId);
        Book book = bookRepository.findById(bookId).orElse(null);
        Objects.requireNonNull(book).setReader(reader);
        book.setBookTakenDate(new Date());
        bookRepository.saveAndFlush(book);
    }
    @Transactional
    public void delete(int id) {
        Book book = bookRepository.findById(id).orElse(null);
        Objects.requireNonNull(book).setReader(null);
        bookRepository.deleteById(id);
    }
}