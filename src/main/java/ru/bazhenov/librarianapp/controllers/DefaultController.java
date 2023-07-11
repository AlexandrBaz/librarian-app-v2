package ru.bazhenov.librarianapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.service.PersonService;

@Controller
public class DefaultController {
    private final PersonService personService;

    public DefaultController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @RequestMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        Person person = personService.getPersonByLogin(request.getUserPrincipal().getName());
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/index";
        } else if (request.isUserInRole("MANAGER")) {
            return "redirect:/manager/index";
        } else if (person.getIsBanned()){
            return "redirect:/banned";
        }
        return "redirect:/user/index";
    }

}
