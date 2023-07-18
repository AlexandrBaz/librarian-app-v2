package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DefaultController {

    @GetMapping()
    public String index() {
        return "/index";
    }

    @GetMapping("/banned")
    public String isBanned() {
        return "/banned";
    }

    @RequestMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/index";
        } else if (request.isUserInRole("MANAGER")) {
            return "redirect:/manager/index";
        }
        return "redirect:/user/index";
    }

}
