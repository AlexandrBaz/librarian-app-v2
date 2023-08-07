package ru.bazhenov.librarianapp.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.service.PersonRepositoryService;

@Component
public class PersonValidator implements Validator {
    private PersonRepositoryService personRepositoryService;
    private OthersUtils othersUtils;

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return PersonDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        PersonDto personDto = (PersonDto) target;
        if (!othersUtils.isCyrillic(personDto.getFullName())){
            errors.rejectValue("fullName","", "ФИО должно быть на русском языке");
        }
        if(personRepositoryService.personByLoginIsPresent(personDto.getLogin())){
            errors.rejectValue("login", "", "Пользователь с таким логином уже существует");
        }
        if (personRepositoryService.PersonByEmailIsPresent(personDto.getEmail())){
            errors.rejectValue("email", "", "Такой email уже используется");
        }
    }

    @Autowired
    public void setPersonRepositoryService(PersonRepositoryService personRepositoryService){
        this.personRepositoryService = personRepositoryService;
    }
    @Autowired
    public void setOthersUtils(OthersUtils othersUtils){
        this.othersUtils = othersUtils;
    }
}
