package ru.bazhenov.librarianapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor(force = true)
@Table(name="book")
@AllArgsConstructor
public class Book extends AbstractEntity {
    @NotEmpty(message = "Введите название книги")
    @Column(name = "book_name", nullable = false)
    private String name;
    @NotEmpty(message = "Введите автора книги")
    @Column(name = "author", nullable = false)
    private String author;
    @Pattern(regexp = "^[12][0-9]{3}$|^[12][0-9]{3}-[12][0-9]{3}$", message = "Введите год выпуска, формат year или year-year")
    @Column(name = "creation_year", nullable = false)
    private String year;
    @Column(name = "books_count", nullable = false)
    private long booksCount;
    @OneToMany(mappedBy = "book", targetEntity = PersonBook.class, cascade = CascadeType.PERSIST)
    private List<PersonBook> personBookList;
}