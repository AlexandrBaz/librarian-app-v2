package ru.bazhenov.librarianapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Entity
@Table(name="book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;
    @NotEmpty(message = "Введите название книги")
    @Column(name = "name", nullable = false)
    private String name;
    @NotEmpty(message = "Введите автора книги")
    @Column(name = "author", nullable = false)
    private String author;
    @Range(min=1000, max=2023, message = "Введите год выпуска")
    @Column(name = "year", nullable = false)
    private int year;
    @Column(name = "book_taken_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date BookTakenDate;
    @Transient
    private Boolean bookTakenIsExpired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id", referencedColumnName = "id")
    private Reader reader;

}