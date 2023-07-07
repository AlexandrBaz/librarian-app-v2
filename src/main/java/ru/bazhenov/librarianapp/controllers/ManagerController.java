package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.service.ManagerService;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/index")
    private String getIndex(HttpServletRequest request, Model model){
        PersonDto managerDto = managerService.getManagerDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("bookDtoExpiredList", managerService.getListOfDebtors());
        model.addAttribute("bannedUserList", managerService.getListBannedUsers());
        return "manager/index";
    }

    @GetMapping("/user-{id}")
    private String getUser(HttpServletRequest request, Model model, @PathVariable("id") long id){
        PersonDto managerDto = managerService.getManagerDto(request.getUserPrincipal().getName());
        model.addAttribute("managerDto", managerDto);
        model.addAttribute("userDto", managerService.getUserDto(id));
        return "manager/user";
    }
}
