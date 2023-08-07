package ru.bazhenov.librarianapp.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.bazhenov.librarianapp.dto.ChangePersonDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.service.PersonRepositoryService;

@Component
public class ChangeProfileValidator implements Validator {
    private PersonRepositoryService personRepositoryService;
    private OthersUtils othersUtils;
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return PersonDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ChangePersonDto personDto = (ChangePersonDto) target;
        if (!othersUtils.isCyrillic(personDto.getFullName())) {
            errors.rejectValue("fullName", "", "ФИО должно быть на русском языке");
        }
        if (personDto.getNewPassword() != null && !personDto.getNewPassword().isBlank()) {
            String oldPasswordFromEntity = personRepositoryService.getPersonByLogin(personDto.getLogin()).getPassword();
            CharSequence oldPasswordFromDto = personDto.getPassword();
            CharSequence newPasswordFromDto = personDto.getNewPassword();
            if (!passwordEncoder.matches(oldPasswordFromDto, oldPasswordFromEntity)){
                errors.rejectValue("password", "", "Для смены пароля укажите старый пароль");
            }
            if (passwordEncoder.matches(newPasswordFromDto, oldPasswordFromEntity)) {
                errors.rejectValue("newPassword", "", "Новый пароль совпадает со старым");
            }
        }
    }

    @Autowired
    public void setOthersUtils(OthersUtils othersUtils){
        this.othersUtils = othersUtils;
    }
    @Autowired
    public void setPersonRepositoryService(PersonRepositoryService personRepositoryService){
        this.personRepositoryService = personRepositoryService;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
}