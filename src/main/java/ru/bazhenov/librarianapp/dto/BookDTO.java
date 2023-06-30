package ru.bazhenov.librarianapp.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ru.bazhenov.librarianapp.models.PersonBook;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO extends AbstractDTO {
    private long id;
    @NotEmpty(message = "Введите название книги")
    private String name;
    @NotEmpty(message = "Введите автора книги")
    private String author;
    @Pattern(regexp = "^[12][0-9]{3}$|^[12][0-9]{3}-[12][0-9]{3}$", message = "Введите год выпуска, формат year или year-year")
    private String year;
    private long booksCount;
    private List<PersonBook> personBookList;
}
