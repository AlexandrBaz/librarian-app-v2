package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.service.AdminService;
import ru.bazhenov.librarianapp.util.PersonValidator;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final PersonValidator personValidator;

    public AdminController(AdminService adminService, PersonValidator personValidator) {
        this.adminService = adminService;
        this.personValidator = personValidator;
    }

    @GetMapping("/index")
    private String getIndex(HttpServletRequest request, Model model){
        PersonDto adminDto = adminService.getAdminDto(request.getUserPrincipal().getName());
        model.addAttribute("adminDto", adminDto);
        model.addAttribute("allUsers", adminService.getAllUsers().size());
        model.addAttribute("allManagers", adminService.getAllManagers().size());
        model.addAttribute("allBooks", adminService.getAllBooks().size());
        model.addAttribute("booksNotReturned", adminService.getBooksNotReturned().size());
        model.addAttribute("bannedUsers", adminService.getBannedUsers().size());
        return "/admin/index";
    }

    @GetMapping("/all-users")
    private String getAllUsers(HttpServletRequest request, Model model){
        PersonDto adminDto = adminService.getAdminDto(request.getUserPrincipal().getName());
        model.addAttribute("adminDto", adminDto);
        model.addAttribute("userDtoList", adminService.getUsersList());
        return "/admin/all-users";
    }

    @GetMapping("/user-{id}")
    private String getUser(HttpServletRequest request, Model model, @PathVariable("id") long id) {
        PersonDto adminDto = adminService.getAdminDto(request.getUserPrincipal().getName());
        PersonDto userDto = adminService.getUserDto(id);
        model.addAttribute("adminDto", adminDto);
        model.addAttribute("userDto", userDto);
        model.addAttribute("bookList", adminService.getUserBooks(userDto.getLogin()));
        return "/admin/user";
    }

    @PatchMapping("/change-status/{id}/{url}")
    private String changeUserStatus(@PathVariable("id") long id, @PathVariable("url") String url) {
        adminService.changePersonStatus(id);
        if(url.contains("manager")){
            return "redirect:/admin/all-managers";
        } else {
        return "redirect:/admin/user-{id}";}
    }

    @GetMapping("/all-managers")
    private String getAllManagers(HttpServletRequest request, Model model){
        PersonDto adminDto = adminService.getAdminDto(request.getUserPrincipal().getName());
        model.addAttribute("adminDto", adminDto);
        model.addAttribute("managerList", adminService.getAllManagers());
        return "/admin/all-managers";
    }

    @GetMapping("/add-manager")
    private String addManager(@ModelAttribute("personDto") PersonDto personDto,
                              HttpServletRequest request, Model model,
                              @RequestParam("status") Optional<String> status){
        PersonDto adminDto = adminService.getAdminDto(request.getUserPrincipal().getName());
        model.addAttribute("adminDto", adminDto);
        if(status.isPresent()){
            String success = status.orElse(null);
            model.addAttribute("success", success);
        }
        return "/admin/add-manager";
    }

    @PostMapping("/add-manager")
    private String createManager(@ModelAttribute("personDto") @Valid PersonDto personDto,
                                 BindingResult bindingResult, HttpServletRequest request, Model model){
        PersonDto adminDto = adminService.getAdminDto(request.getUserPrincipal().getName());
        model.addAttribute("adminDto", adminDto);
        personValidator.validate(personDto, bindingResult);
        if(bindingResult.hasErrors()){
            return "/admin/add-manager";
        }
        adminService.registerNewUser(personDto);
        return "redirect:/admin/add-manager?status=ok";
    }
}
