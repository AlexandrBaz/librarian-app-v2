package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.PageableData;
import ru.bazhenov.librarianapp.service.UserService;
import ru.bazhenov.librarianapp.util.ChangeProfileValidator;
import ru.bazhenov.librarianapp.util.Pagination;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private ChangeProfileValidator changeProfileValidator;
    private Pagination pagination;


    @GetMapping("/index")
    public String getIndex(@NotNull HttpServletRequest request, Model model) {
        PersonDto userDto = userService.getProfileDto(request.getUserPrincipal().getName());
        if (userDto.getIsBanned()){
            return "redirect:/banned";
        }
        model.addAttribute("userDto", userDto);
        if (!userDto.getPersonBookList().isEmpty()){
            model.addAttribute("bookList", userService.getUserBooks(request.getUserPrincipal().getName()));
        }
        return "/user/index";
    }

    @GetMapping(value = "/books")
    public String getBooks(@NotNull HttpServletRequest request,
                           Model model,
                           @RequestParam("search") Optional<String> search,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("by") Optional<String> sortBy) {
        PersonDto userDto = userService.getProfileDto(request.getUserPrincipal().getName());
        if (userDto.getIsBanned()){
            return "redirect:/banned";
        }
        model.addAttribute("userDto", userDto);

        if(search.isPresent()){
            String key = search.map(Object::toString).orElse(null);
            if (!key.isBlank()) {
                model.addAttribute("booksSearch", userService.searchBook(key, userDto));
            }
        }

        PageableData pageableData = new PageableData();
        pageableData.setCurrenPage(page.orElse(1));
        pageableData.setPageSize(size.orElse(5));
        pageableData.setSort(sortBy.orElse("name"));
        Page<BookDto> availableBookPage = pagination.getPaginatedPage(pageableData, userDto);
        model.addAttribute("bookDtoList", availableBookPage);
        int totalPages = availableBookPage.getTotalPages() + 1;
        if (totalPages > 0){
            model.addAttribute("pageNumbers", pagination.getPageNumbers(totalPages));
        }
        model.addAttribute("sortBy", sortBy.map(Object::toString).orElse(null));
        return "/user/books";
    }

    @PatchMapping("/books/add-book-{id}")
    public String userProfileAddBook(@NotNull HttpServletRequest request, @PathVariable("id") long bookId) {
        userService.addBookToUser(request.getUserPrincipal().getName(), bookId);
        return "redirect:/user/books";
    }

    @PatchMapping("/books/return-book-{id}")
    public String userProfileReturnBook(@NotNull HttpServletRequest request, @PathVariable("id") long bookId) {
        userService.returnBookFromPerson(request.getUserPrincipal().getName(), bookId);
        return "redirect:/user/index";
    }

    @GetMapping("/settings")
    public String changeUserProfile(@NotNull HttpServletRequest request, Model model, @RequestParam("status") Optional<String> status){
        PersonDto userDto = userService.getProfileDto(request.getUserPrincipal().getName());
        if (userDto.getIsBanned()){
            return "redirect:/banned";
        }
        model.addAttribute("userDto", userDto);
        if(status.isPresent()){
            String changeSave = status.orElse(null);
            model.addAttribute("changeSave", changeSave);
        }
        return "/user/settings";
    }

    @PatchMapping("/settings")
    public String updateUserProfile(@ModelAttribute("userDto") @Valid @NotNull ChangePersonDto userDto,
                                    BindingResult bindingResult, @NotNull HttpServletRequest request){
        PersonDto currentUserDto = userService.getProfileDto(request.getUserPrincipal().getName());
        userDto.setLogin(currentUserDto.getLogin());
        changeProfileValidator.validate(userDto, bindingResult);
        userDto.setPersonBookList(userService.getProfileDto(request.getUserPrincipal().getName()).getPersonBookList());
        if(bindingResult.hasErrors()){
            return "/user/settings";
        }
        userService.updateUser(userDto);
        return "redirect:/user/settings?status=ok";
    }

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    @Autowired
    public void setPagination(Pagination pagination){
        this.pagination = pagination;
    }
    @Autowired
    public void setChangeProfileValidator(ChangeProfileValidator changeProfileValidator){
        this.changeProfileValidator = changeProfileValidator;
    }
}
