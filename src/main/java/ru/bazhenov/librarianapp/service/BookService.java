package ru.bazhenov.librarianapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.repositories.BookRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBook(long id){
       return bookRepository.getReferenceById(id);
    }

    public List<Book> getBooks(){
        return bookRepository.findAllByBooksCountGreaterThan(0L);
    }

    public List<Book> getAvailableBookForUserProfile(long userId){
        List<Book> available = bookRepository.findAllBookForUserById(userId);
        available.forEach(book ->{
            System.out.println(book.getName());
        });
        return bookRepository.findAllBookForUserById(userId);
    }

    @Transactional
    public void reduceBookCount(long bookId){
        Book book = getBook(bookId);
        book.setBooksCount(book.getBooksCount()-1);
        bookRepository.saveAndFlush(book);
    }

    public void increaseBookCount(long bookId) {
        Book book = getBook(bookId);
        book.setBooksCount(book.getBooksCount()+1);
        bookRepository.saveAndFlush(book);
    }


//    public List<Book> index() {
//        return bookRepository.findAll(Sort.by("author"));
//    }
//    public PaginationAndSort index(int offset, int limit){
//        PaginationAndSort paginationAndSort = new PaginationAndSort();
//        paginationAndSort.setCurrentPage(offset+1);
//        paginationAndSort.setNumberOfPage(bookRepository.findAll(PageRequest.of(offset,limit)).getTotalPages());
//        paginationAndSort.setBookPage(bookRepository.findAll(PageRequest.of(offset,limit,Sort.by("id"))));
//        return paginationAndSort;
//    }
//    public PaginationAndSort paginationAndSort(int offset, int limit){
//        PaginationAndSort paginationAndSort = new PaginationAndSort();
//        paginationAndSort.setCurrentPage(offset+1);
//        paginationAndSort.setNumberOfPage(bookRepository.findAll(PageRequest.of(offset,limit)).getTotalPages());
//        paginationAndSort.setBookPage(bookRepository.findAll(PageRequest.of(offset,limit,Sort.by("year"))));
//        return paginationAndSort;
//    }
//    public List<Book> indexSortByYear(){
//        return bookRepository.findAll(Sort.by("year"));
//    }
//    public List<Book> search(String search){
//        return bookRepository.findAllByNameContainsIgnoreCase(search);
//    }
////    @Transactional
////    public void addNewBook(Book book) {
////        bookRepository.saveAndFlush(book);
////    }
////
////    public Book getBookById(int id){
////        return bookRepository.findById(id).orElse(null);
////    }
////
////    public BookAndReaders getBookList(int id){
////        BookAndReaders bookAndReaders = new BookAndReaders();
////        Book book = bookRepository.findById(id).orElse(null);
////        bookAndReaders.setBook(book);
////        bookAndReaders.setReaderList(readerService.index());
////        bookAndReaders.setThisBookReader(Objects.requireNonNull(bookRepository.findById(id).orElse(null)).getReader());
////        bookAndReaders.setBookIsTaken(Objects.requireNonNull(book).getReader() !=null);
////        return bookAndReaders;
////    }
////
////    @Transactional
////    public void updateBook(int id, @NotNull Book updatedBook) {
////        updatedBook.setId(id);
////        bookRepository.saveAndFlush(updatedBook);
////    }
////    @Transactional
////    public void resetReader(int id) {
////        Book book = bookRepository.findById(id).orElse(null);
////        Objects.requireNonNull(book).setReader(null);
////        book.setBookTakenDate(null);
////        bookRepository.saveAndFlush(book);
////    }
////    @Transactional
////    public void setBookReader(int readerId, int bookId) {
////        Reader reader = readerService.getByIdReader(readerId);
////        Book book = bookRepository.findById(bookId).orElse(null);
////        Objects.requireNonNull(book).setReader(reader);
////        book.setBookTakenDate(new Date());
////        bookRepository.saveAndFlush(book);
////    }
////    @Transactional
////    public void delete(int id) {
////        Book book = bookRepository.findById(id).orElse(null);
////        Objects.requireNonNull(book).setReader(null);
////        bookRepository.deleteById(id);
////    }
}