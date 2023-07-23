package ru.bazhenov.librarianapp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.PersonRole;
import ru.bazhenov.librarianapp.service.PersonServiceImpl;
import ru.bazhenov.librarianapp.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PersonServiceImpl personServiceImpl;
    private final PersonValidator personValidator;

    @Autowired
    public AuthController(PersonServiceImpl personServiceImpl, PersonValidator personValidator) {
        this.personServiceImpl = personServiceImpl;
        this.personValidator = personValidator;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("personDto") PersonDto personDto){
        return "auth/registration";
    }
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("personDto") @Valid PersonDto personDto, BindingResult bindingResult){
        personValidator.validate(personDto, bindingResult);
        if(bindingResult.hasErrors()){
            return "auth/registration";
        }
        personServiceImpl.registerNewUser(personDto, PersonRole.USER);
        return "redirect:/auth/login";
    }
}
