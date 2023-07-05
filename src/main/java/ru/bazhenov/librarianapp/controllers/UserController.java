package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.service.UserService;
import ru.bazhenov.librarianapp.util.ChangeProfileValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ChangeProfileValidator changeProfileValidator;


    @Autowired
    public UserController(UserService userService, ChangeProfileValidator changeProfileValidator) {
        this.userService = userService;
        this.changeProfileValidator = changeProfileValidator;
    }

    @GetMapping("/index")
    public String getIndex(HttpServletRequest request, Model model) {
        PersonDto userDto = userService.getUserDto(request.getUserPrincipal().getName());
        model.addAttribute("userDto", userDto);
        if (!userDto.getPersonBookList().isEmpty()){
            model.addAttribute("bookList", userService.getUserBooks(userDto));
        }
        return "user/index";
    }

    @GetMapping(value = "/books")
    public String getBooks(HttpServletRequest request,
                           Model model,
                           @RequestParam("search") Optional<String> search,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("by") Optional<String> sortBy) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String sort = sortBy.orElse("id");
        PersonDto userDto = userService.getUserDto(request.getUserPrincipal().getName());
        if(search.isPresent()){
            String key = search.map(Object::toString).orElse(null);
            model.addAttribute("booksSearch", userService.searchBook(key, userDto));
        }
        Page<BookDto> bookPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize), userDto, sort);
        model.addAttribute("bookDtoList", bookPage);
        int totalPages = bookPage.getTotalPages() + 1;
        if (totalPages > 0){
            List<Integer> pageNumbers = IntStream.range(1,totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("userDto", userDto);
        return "user/books";
    }
    @PatchMapping("/books/add-book-{id}")
    public String userProfileAddBook(HttpServletRequest request, @PathVariable("id") long bookId) {
        userService.addBookToUser(request.getUserPrincipal().getName(), bookId);
        return "redirect:/user/books";
    }

    @PatchMapping("/books/return-book-{id}")
    public String userProfileReturnBook(HttpServletRequest request, @PathVariable("id") long bookId) {
        userService.returnBookFromPerson(request.getUserPrincipal().getName(), bookId);
        return "redirect:/user/index";
    }

    @GetMapping("/settings")
    public String changeUserProfile(HttpServletRequest request, Model model){
        PersonDto userDto = userService.getUserDto(request.getUserPrincipal().getName());
        model.addAttribute("userDto", userDto);
        return "/user/settings";
    }

    @PostMapping("/settings")
    public String updateUserProfile(@ModelAttribute("userDto") @Valid PersonDto userDto,
                                    BindingResult bindingResult, HttpServletRequest request){
        changeProfileValidator.validate(userDto, bindingResult);
        userDto.setLogin(request.getUserPrincipal().getName());
        userDto.setPersonBookList(userService.getUserDto(request.getUserPrincipal().getName()).getPersonBookList());
        if(bindingResult.hasErrors()){
            return "/user/settings";
        }
        userService.updateUser(userDto);
        return "redirect:/user/settings";
    }
}

//TODO ResponseEntity внедрить
