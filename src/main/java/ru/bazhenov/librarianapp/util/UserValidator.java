package ru.bazhenov.librarianapp.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.bazhenov.librarianapp.models.UserProfile;
import ru.bazhenov.librarianapp.service.UserProfileService;

@Component
public class UserValidator implements Validator {
    private final UserProfileService userProfileService;
    private final OthersUtils othersUtils;

    @Autowired
    public UserValidator(UserProfileService userProfileService, OthersUtils othersUtils) {
        this.userProfileService = userProfileService;
        this.othersUtils = othersUtils;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return UserProfile.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        UserProfile userProfile = (UserProfile) target;
        if (!othersUtils.isCyrillic(userProfile.getFullName())){
            errors.rejectValue("fullName","", "ФИО должно быть на русском языке");
        }
        if(userProfileService.getUserByLogin(userProfile.getUserName())){
            errors.rejectValue("login", "", "Пользователь с таким логином уже существует");
        }
        if (userProfileService.getUserByEmail(userProfile.getEmail())){
            errors.rejectValue("email", "", "Такой email уже используется");
        }

    }
}
