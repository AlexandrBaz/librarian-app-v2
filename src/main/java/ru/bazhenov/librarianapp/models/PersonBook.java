package ru.bazhenov.librarianapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "person_book")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class PersonBook extends AbstractEntity{
    @Column(name = "person_book_date")
    @Temporal(TemporalType.DATE)
    private LocalDate personBookDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book", referencedColumnName = "id")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person", referencedColumnName = "id")
    private Person person;
}