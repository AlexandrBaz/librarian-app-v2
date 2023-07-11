package ru.bazhenov.librarianapp.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.bazhenov.librarianapp.dto.BookDto;
@Component
public class BookValidator implements Validator{

    private final OthersUtils othersUtils;

    @Autowired
    public BookValidator(OthersUtils othersUtils) {
        this.othersUtils = othersUtils;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return BookDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        BookDto bookDto = (BookDto) target;
        if (!othersUtils.isCyrillic(bookDto.getName())){
            errors.rejectValue("name","", "Название книги должно быть на русском языке");
        }
        if(!othersUtils.isCyrillic(bookDto.getAuthor())){
            errors.rejectValue("author","", "Имя автора на должно быть на русском языке");
        }
        if(bookDto.getBooksCount() <= 0L ){
            errors.rejectValue("booksCount","", "Количество книг должно быть больше нуля");
        }
    }
}

