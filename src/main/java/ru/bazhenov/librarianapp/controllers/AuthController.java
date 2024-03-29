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
import ru.bazhenov.librarianapp.service.PersonRepositoryService;
import ru.bazhenov.librarianapp.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private PersonRepositoryService personRepositoryService;
    private PersonValidator personValidator;

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
        personRepositoryService.registerNewUser(personDto, PersonRole.USER);
        return "redirect:/auth/login";
    }

    @Autowired
    public void setPersonRepositoryService(PersonRepositoryService personRepositoryService){
        this.personRepositoryService = personRepositoryService;
    }
    @Autowired
    public void setPersonValidator(PersonValidator personValidator){
        this.personValidator = personValidator;
    }
}
