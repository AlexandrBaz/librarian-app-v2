package ru.bazhenov.librarianapp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.service.PersonService;
import ru.bazhenov.librarianapp.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public AuthController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person){
        return "auth/registration";
    }
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "auth/registration";
        }
        personService.registerNewUser(person);
        return "redirect:/auth/login";
    }
}
