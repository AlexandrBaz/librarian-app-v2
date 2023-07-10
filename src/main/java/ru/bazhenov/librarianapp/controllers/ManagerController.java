package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.PageableData;
import ru.bazhenov.librarianapp.service.ManagerService;
import ru.bazhenov.librarianapp.service.UserService;
import ru.bazhenov.librarianapp.util.Pagination;

import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;
    private final UserService userService;
    private final Pagination pagination;

    @Autowired
    public ManagerController(ManagerService managerService, UserService userService, Pagination pagination) {
        this.managerService = managerService;
        this.userService = userService;
        this.pagination = pagination;
    }

    @GetMapping("/index")
    private String getIndex(HttpServletRequest request, Model model) {
        PersonDto managerDto = managerService.getManagerDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("bookDtoExpiredList", managerService.getListOfDebtors());
        model.addAttribute("bannedUserList", managerService.getListBannedUsers());
        return "/manager/index";
    }

    @GetMapping("/user-{id}")
    private String getUser(HttpServletRequest request, Model model, @PathVariable("id") long id) {
        PersonDto managerDto = managerService.getManagerDto(request.getUserPrincipal().getName());
        PersonDto userDto = managerService.getUserDto(id);
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("userDto", userDto);
        model.addAttribute("bookList", userService.getUserBooks(userDto.getLogin()));
        return "/manager/user";
    }

    @PatchMapping("/change-status-{id}")
    private String changeUserStatus(@PathVariable("id") long id) {
        managerService.changePersonStatus(id);
        return "redirect:/manager/user-{id}";
    }

    @GetMapping("/books")
    private String allBooks(HttpServletRequest request, Model model,
                            @RequestParam("search") Optional<String> search,
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

        PersonDto managerDto = managerService.getManagerDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("sortBy", sortBy.map(Object::toString).orElse(null));
        return "/manager/books";
    }
}
