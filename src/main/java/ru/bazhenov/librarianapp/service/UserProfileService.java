package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.dto.UserProfileDTO;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.BookTaken;
import ru.bazhenov.librarianapp.models.UserProfile;
import ru.bazhenov.librarianapp.models.UserProfileRole;
import ru.bazhenov.librarianapp.repositories.UserProfileRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final static int bookDaysToExpired = 10;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfile getUserProfileById(long id) {
        return userProfileRepository.getReferenceById(id);
    }

    public Boolean getUserByLogin(String userNane) {
        return userProfileRepository.findUserByUserName(userNane).isPresent();
    }

    public Boolean getUserByEmail(String email) {
        return userProfileRepository.findUserByEmail(email).isPresent();
    }

    @Transactional
    public void registerNewUser(UserProfile userProfile) {
        userProfile.setPassword(passwordEncoder.encode(userProfile.getPassword()));
        userProfile.setUserProfileRole(UserProfileRole.USER);
        userProfile.setIsBanned(false);
        userProfile.setPassIsExpired(false);
        userProfileRepository.saveAndFlush(userProfile);
    }

    public UserProfileDTO ensureUserProfile(UserProfileDTO userProfileDTO) {
        List<BookTaken> bookTakenList = userProfileDTO.getBookTakenList();
        if (!bookTakenList.isEmpty()) {
            bookTakenList.forEach(bookTaken -> {
                bookTaken.setBookTakenDateExpiration(bookDaysToExpired);
                bookTaken.setBookTakenIsExpired(bookIsExpired(bookTaken.getBookTakenDate()));
            });
        }
        return userProfileDTO;
    }

    public Boolean bookIsExpired(Date dateTaken) {
        LocalDate bookTakenDate = dateTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(bookTakenDate, LocalDate.now()) > bookDaysToExpired;
    }

    public List<Book> getListEnableToTake(UserProfileDTO userProfileDTO, List<Book> books) {
        List<Book> bookTakenList = userProfileDTO.getBookTakenList().stream()
                .map(BookTaken::getBook)
                .toList();
        return books.stream()
                .filter(book -> !bookTakenList.contains(book))
                .toList();
    }
}
