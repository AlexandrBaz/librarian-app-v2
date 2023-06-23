package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bazhenov.librarianapp.dto.UserProfileDTO;
import ru.bazhenov.librarianapp.models.UserProfile;
import ru.bazhenov.librarianapp.repositories.UserProfileRepository;
import ru.bazhenov.librarianapp.service.BookService;
import ru.bazhenov.librarianapp.service.BookTakenService;
import ru.bazhenov.librarianapp.service.UserProfileService;

import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileService userProfileService;
    private final BookService bookService;
    private final BookTakenService bookTakenService;

    public UserController(UserProfileRepository userProfileRepository, UserProfileService userProfileService, BookService bookService, BookTakenService bookTakenService) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileService = userProfileService;
        this.bookService = bookService;
        this.bookTakenService = bookTakenService;
    }

    @RequestMapping("/index")
    public String getIndex(HttpServletRequest request, Model model){
        UserProfile userProfile = userProfileRepository.findUserByUserName(request.getUserPrincipal().getName()).orElse(null);
        UserProfileDTO userProfileDTO = convertToDTO(Objects.requireNonNull(userProfile));
        model.addAttribute("userProfile", userProfileService.ensureUserProfile(userProfileDTO));
        return "user/index";
    }

    @RequestMapping("/books")
    public String getBooks(HttpServletRequest request, Model model){
        UserProfile userProfile = userProfileRepository.findUserByUserName(request.getUserPrincipal().getName()).orElse(null);
        UserProfileDTO userProfileDTO = convertToDTO(Objects.requireNonNull(userProfile));
        model.addAttribute("books", userProfileService.getListEnableToTake(userProfileDTO, bookService.getBooks()));
//        model.addAttribute("userProfile", userProfileDTO);
        return "user/books";
    }

    @RequestMapping("/books/add-book-{id}")
    public String userProfileAddBook(HttpServletRequest request, @PathVariable("id") long bookId){
        UserProfile userProfile = userProfileRepository.findUserByUserName(request.getUserPrincipal().getName()).orElse(null);
        bookTakenService.addBookToUser(Objects.requireNonNull(userProfile).getId(),bookId);
        return "redirect:/user/books";
    }

    @RequestMapping("/books/return-book-{id}")
    public String userProfileReturnBook(HttpServletRequest request, @PathVariable("id") long bookId){
        UserProfile userProfile = userProfileRepository.findUserByUserName(request.getUserPrincipal().getName()).orElse(null);
        bookTakenService.returnBookFromUser(Objects.requireNonNull(userProfile), bookService.getBook(bookId));
        return "redirect:/user/index";
    }

    private UserProfileDTO convertToDTO(UserProfile userProfile){
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(userProfile.getId());
        userProfileDTO.setUserName(userProfile.getUserName());
        userProfileDTO.setFullName(userProfile.getFullName());
        userProfileDTO.setYearOfBirth(userProfile.getYearOfBirth());
        userProfileDTO.setBookTakenList(userProfile.getBookTakenList());
        userProfileDTO.setEmail(userProfile.getEmail());
        return  userProfileDTO;
    }



}