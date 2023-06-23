package ru.bazhenov.librarianapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.BookTaken;
import ru.bazhenov.librarianapp.models.UserProfile;

import java.util.Optional;
@Component
public interface BookTakenRepository extends JpaRepository<BookTaken, Long> {
    Optional<BookTaken> findFirstByUserProfileAndBook(UserProfile userProfile, Book book);
}
