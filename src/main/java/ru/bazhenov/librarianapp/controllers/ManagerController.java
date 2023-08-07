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
import ru.bazhenov.librarianapp.service.ManagerService;
import ru.bazhenov.librarianapp.util.BookValidator;
import ru.bazhenov.librarianapp.util.ChangeProfileValidator;
import ru.bazhenov.librarianapp.util.Pagination;

import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private ManagerService managerService;
    private Pagination pagination;
    private BookValidator bookValidator;
    private ChangeProfileValidator changeProfileValidator;

    @GetMapping("/index")
    private @NotNull String getIndex(@NotNull HttpServletRequest request, @NotNull Model model) {
        PersonDto managerDto = managerService.getProfileDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("bookDtoExpiredList", managerService.getListOfDebtors());
        model.addAttribute("bannedUserList", managerService.getListBannedUsers());
        return "/manager/index";
    }

    @GetMapping("/user-{id}")
    private @NotNull String getUser(@NotNull HttpServletRequest request, @NotNull Model model, @PathVariable("id") long id) {
        PersonDto managerDto = managerService.getProfileDto(request.getUserPrincipal().getName());
        PersonDto userDto = managerService.getUserDto(id);
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("userDto", userDto);
        model.addAttribute("bookList", managerService.getUserBooks(userDto.getLogin()));
        return "/manager/user";
    }

    @PatchMapping("/change-status-{id}")
    private @NotNull String changeUserStatus(@PathVariable("id") long id) {
        managerService.changePersonStatus(id);
        return "redirect:/manager/user-{id}";
    }

    @GetMapping("/books")
    private @NotNull String allBooks(HttpServletRequest request, Model model,
                                     @RequestParam("search") @NotNull Optional<String> search,
                                     @RequestParam("page") Optional<Integer> page,
                                     @RequestParam("size") Optional<Integer> size,
                                     @RequestParam("by") Optional<String> sortBy) {
        if (search.isPresent()) {
            String key = search.map(Object::toString).orElse(null);
            if (!key.isBlank()) {
                model.addAttribute("booksSearch", managerService.searchBook(key.trim()));
            }
        }
        PageableData pageableData = new PageableData();
        pageableData.setCurrenPage(page.orElse(1));
        pageableData.setPageSize(size.orElse(10));
        pageableData.setSort(sortBy.orElse("name"));
        Page<BookDto> bookPage = pagination.getPaginatedPage(pageableData);
        model.addAttribute("bookDtoList", bookPage);
        int totalPages = bookPage.getTotalPages() + 1;
        if (totalPages > 0) {
            model.addAttribute("pageNumbers", pagination.getPageNumbers(totalPages));
        }

        PersonDto managerDto = managerService.getProfileDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("sortBy", sortBy.map(Object::toString).orElse(null));
        return "/manager/books";
    }

    @GetMapping("/all-users")
    public String getAllUsers(@NotNull HttpServletRequest request, @NotNull Model model){
        PersonDto managerDto = managerService.getProfileDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("userDtoList", managerService.getAllUsers());
        return "/manager/all-users";
    }

    @GetMapping("/add-books")
    public String addBooks(@NotNull HttpServletRequest request, @ModelAttribute("bookDto") BookDto bookDto,
                           @NotNull Model model, @RequestParam("status") @NotNull Optional<String> status){
        PersonDto managerDto = managerService.getProfileDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        if(status.isPresent()){
            String bookSave = status.orElse(null);
            model.addAttribute("bookSave", bookSave);
        }
        return "/manager/add-books";
    }

    @PostMapping("/add-books")
    public String postBook(@NotNull HttpServletRequest request, @NotNull Model model,
                           @ModelAttribute("bookDto") @Valid BookDto bookDto, BindingResult bindingResult){
        PersonDto managerDto = managerService.getProfileDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        bookValidator.validate(bookDto, bindingResult);
        if(bindingResult.hasErrors()){
            return "/manager/add-books";
        }
        managerService.addBook(bookDto);
        return "redirect:/manager/add-books?status=ok";
    }

    @GetMapping("/settings")
    private @NotNull String managerSettings(@NotNull HttpServletRequest request, @NotNull Model model, @RequestParam("status") @NotNull Optional<String> status){
        PersonDto managerDto = managerService.getProfileDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        if(status.isPresent()){
            String changeSave = status.orElse(null);
            model.addAttribute("changeSave", changeSave);
        }
        return "/manager/settings";
    }

    @PatchMapping("/settings")
    private @NotNull String changeProfile(@ModelAttribute("managerDto") @Valid @NotNull ChangePersonDto managerDto,
                                          BindingResult bindingResult, @NotNull HttpServletRequest request){
        PersonDto currentManagerDto = managerService.getProfileDto(request.getUserPrincipal().getName());
        managerDto.setLogin(currentManagerDto.getLogin());
        changeProfileValidator.validate(managerDto, bindingResult);
        if(bindingResult.hasErrors()){
            return "/manager/settings";
        }
        managerService.updateUser(managerDto);
        return "redirect:/manager/settings?status=ok";
    }

    @Autowired
    public void setManagerService(ManagerService managerService){
        this.managerService = managerService;
    }
    @Autowired
    public void setPagination(Pagination pagination){
        this.pagination = pagination;
    }
    @Autowired
    public void setBookValidator(BookValidator bookValidator){
        this.bookValidator = bookValidator;
    }
    @Autowired
    public void setChangeProfileValidator(ChangeProfileValidator changeProfileValidator){
        this.changeProfileValidator = changeProfileValidator;
    }

}