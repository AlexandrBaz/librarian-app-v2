package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.BookTaken;
import ru.bazhenov.librarianapp.models.UserProfile;
import ru.bazhenov.librarianapp.repositories.BookTakenRepository;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class BookTakenService {

    private final BookTakenRepository bookTakenRepository;
    private final UserProfileService userProfileService;
    private final BookService bookService;

    @Autowired
    public BookTakenService(BookTakenRepository bookTakenRepository, UserProfileService userProfileService, BookService bookService) {
        this.bookTakenRepository = bookTakenRepository;
        this.userProfileService = userProfileService;
        this.bookService = bookService;
    }

    @Transactional
    public void addBookToUser(long userProfileId, long bookId) {
        BookTaken bookTaken = new BookTaken();
        bookTaken.setBookTakenDate(new Date());
        bookTaken.setUserProfile(userProfileService.getUserProfileById(userProfileId));
        bookTaken.setBook(bookService.getBook(bookId));

        bookTakenRepository.saveAndFlush(bookTaken);
        bookService.reduceBookCount(bookId);
    }

    @Transactional
    public void returnBookFromUser(UserProfile userProfile, Book book) {

        BookTaken bookTaken = bookTakenRepository.findFirstByUserProfileAndBook(userProfile,book).orElse(null);
        bookTakenRepository.delete(Objects.requireNonNull(bookTaken));
        bookService.increaseBookCount(book.getId());
    }
}
////    @Transactional
////    public void setBookReader(int readerId, int bookId) {
////        Reader reader = readerService.getByIdReader(readerId);
////        Book book = bookRepository.findById(bookId).orElse(null);
////        Objects.requireNonNull(book).setReader(reader);
////        book.setBookTakenDate(new Date());
////        bookRepository.saveAndFlush(book);
////    }
