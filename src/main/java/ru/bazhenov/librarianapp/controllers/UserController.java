package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bazhenov.librarianapp.dto.BookDTO;
import ru.bazhenov.librarianapp.dto.PersonDTO;
import ru.bazhenov.librarianapp.mapper.PersonMapper;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.service.BookService;
import ru.bazhenov.librarianapp.service.PersonBookService;
import ru.bazhenov.librarianapp.service.PersonService;
import ru.bazhenov.librarianapp.service.UserService;
import ru.bazhenov.librarianapp.util.OthersUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
public class UserController {
    private final PersonService personService;
    private final BookService bookService;
    private final PersonBookService personBookService;
    private final UserService userService;
    private final OthersUtils othersUtils;
    private final PersonMapper personMapper;


    @Autowired
    public UserController(PersonService personService, BookService bookService, PersonBookService personBookService, UserService userService, OthersUtils othersUtils, PersonMapper personMapper) {
        this.personService = personService;
        this.bookService = bookService;
        this.personBookService = personBookService;
        this.userService = userService;
        this.othersUtils = othersUtils;
        this.personMapper = personMapper;
    }

    @RequestMapping("/index")
    public String getIndex(HttpServletRequest request, Model model) {
        PersonDTO userDto = userService.getUserDto(request.getUserPrincipal().getName());
        model.addAttribute("person", userDto);
        if (!userDto.getPersonBookList().isEmpty()){
            model.addAttribute("bookList", userService.getUserBooks(userDto));
        }

        return "user/index";
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String getBooks(HttpServletRequest request,
                           Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("by") Optional<String> sortBy) {
        Person person = personService.getPersonByLogin(request.getUserPrincipal().getName());

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String sort = sortBy.orElse("id");

        Page<BookDTO> bookPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize), person, sort);
        model.addAttribute("bookPage", bookPage);
        int totalPages = bookPage.getTotalPages()+1;
        if (totalPages > 0){
            List<Integer> pageNumbers = IntStream.range(1,totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
//        model.addAttribute("books", personService.findPaginated());
        model.addAttribute("person", userService.getListEnableToTake(person, sort));
        return "user/books";
    }

    @RequestMapping("/books/add-book-{id}")
    public String userProfileAddBook(HttpServletRequest request, @PathVariable("id") long bookId) {
        Person person = personService.getPersonByLogin(request.getUserPrincipal().getName());
        personBookService.addBookToUser(Objects.requireNonNull(person).getId(), bookId);
        return "redirect:/user/books";
    }

    @RequestMapping("/books/return-book-{id}")
    public String userProfileReturnBook(HttpServletRequest request, @PathVariable("id") long bookId) {
        Person person = personService.getPersonByLogin(request.getUserPrincipal().getName());
        personBookService.returnBookFromPerson(Objects.requireNonNull(person), bookService.getBook(bookId));
        return "redirect:/user/index";
    }
}