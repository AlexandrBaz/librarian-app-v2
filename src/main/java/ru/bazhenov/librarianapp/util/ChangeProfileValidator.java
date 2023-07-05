package ru.bazhenov.librarianapp.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.service.PersonService;

@Component
public class ChangeProfileValidator implements Validator {
    private final PersonService personService;
    private final OthersUtils othersUtils;
    private final PasswordEncoder passwordEncoder;

    public ChangeProfileValidator(PersonService personService, OthersUtils othersUtils, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.othersUtils = othersUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return PersonDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        PersonDto personDto = (PersonDto) target;
        if (!othersUtils.isCyrillic(personDto.getFullName())) {
            errors.rejectValue("fullName", "", "ФИО должно быть на русском языке");
        }
        if (personDto.getNewPassword() != null) {
            String oldPasswordFromEntity = personService.getPersonByLogin(personDto.getLogin()).getPassword();
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
}