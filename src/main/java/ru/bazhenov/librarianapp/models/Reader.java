package ru.bazhenov.librarianapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
@Entity
@Table(name = "reader")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;
    @NotEmpty(message = "Введите Фамилию Имя Отчество")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "Формат ввода, с большой буквы, разделенный пробелами")
    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;
    @Column(name = "year_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date yearOfBirth;
    @OneToMany(mappedBy = "reader", targetEntity = Book.class, cascade = CascadeType.PERSIST)
    private List<Book> bookList;

    public void addBook(Book book){
        bookList.add(book);
        book.setReader(this);
    }
    public void removeBook(Book book){
        bookList.remove(book);
        book.setReader(null);
        book.setBookTakenDate(null);
    }

}
