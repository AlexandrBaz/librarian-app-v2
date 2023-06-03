package ru.bazhenov.librarianapp.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.bazhenov.librarianapp.models.Reader;
import ru.bazhenov.librarianapp.service.ReaderService;


@Component
public class ReaderValidator implements Validator {
    private final BookValidator bookValidator;
    private final ReaderService readerService;
    @Autowired
    public ReaderValidator(BookValidator bookValidator, ReaderService readerService) {
        this.bookValidator = bookValidator;
        this.readerService = readerService;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return Reader.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        Reader reader = (Reader) target;
        if (!isStringCyrillic(reader.getFullName())){
            errors.rejectValue("fullName","", "ФИО должно быть на русском языке");
        }
        if (readerService.findReaderByName(reader.getFullName()) && readerService.findReaderByBirth(reader.getYearOfBirth())){
            errors.rejectValue("fullName", "", "Такой пользователь уже существует");
        }
    }
    private Boolean isStringCyrillic(String fullName){
        return bookValidator.isStringCyrillic(fullName);
    }
}
