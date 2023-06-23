package ru.bazhenov.librarianapp.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.bazhenov.librarianapp.models.Book;
@Component
public class BookValidator implements Validator{

    private final OthersUtils othersUtils;

    @Autowired
    public BookValidator(OthersUtils othersUtils) {
        this.othersUtils = othersUtils;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        Book book = (Book) target;
        if (!othersUtils.isCyrillic(book.getName())){
            errors.rejectValue("fullName","", "Название книги должно быть на русском языке");
        }
        if(!othersUtils.isCyrillic(book.getAuthor())){
            errors.rejectValue("author","", "Имя автора на должно быть на русском языке");
        }
    }
}

