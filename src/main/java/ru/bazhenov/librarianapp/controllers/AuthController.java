package ru.bazhenov.librarianapp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bazhenov.librarianapp.models.UserProfile;
import ru.bazhenov.librarianapp.service.UserProfileService;
import ru.bazhenov.librarianapp.util.UserValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserProfileService userProfileService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(UserProfileService userProfileService, UserValidator userValidator) {
        this.userProfileService = userProfileService;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("userProfile") UserProfile userProfile){
        return "auth/registration";
    }
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("userProfile") @Valid UserProfile userProfile, BindingResult bindingResult){
        userValidator.validate(userProfile, bindingResult);
        if(bindingResult.hasErrors()){
            return "auth/registration";
        }
        userProfileService.registerNewUser(userProfile);
        return "redirect:/auth/login";
    }
}
