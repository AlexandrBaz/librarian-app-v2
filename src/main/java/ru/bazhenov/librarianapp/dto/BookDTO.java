package ru.bazhenov.librarianapp.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bazhenov.librarianapp.models.PersonBook;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BookDTO {
    private long id;
    @NotEmpty(message = "Введите название книги")
    private String name;
    @NotEmpty(message = "Введите автора книги")
    private String author;
    @Pattern(regexp = "^[12][0-9]{3}$|^[12][0-9]{3}-[12][0-9]{3}$", message = "Введите год выпуска, формат year или year-year")
    private String year;
    @Column(name = "books_count", nullable = false)
    private long booksCount;
    @OneToMany(mappedBy = "book", targetEntity = PersonBook.class, cascade = CascadeType.PERSIST)
    private List<PersonBookDTO> personBookList;
}
