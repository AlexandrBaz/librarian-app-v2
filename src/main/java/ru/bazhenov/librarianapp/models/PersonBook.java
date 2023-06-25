package ru.bazhenov.librarianapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "person_book")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class PersonBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_book_id", nullable = false, unique = true)
    private long id;
    @Column(name = "person_book_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date personBookDate;
    @Transient
    @Temporal(TemporalType.DATE)
    private Date personBookDateExpiration;
    @Transient
    private Boolean personBookIsExpired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book", referencedColumnName = "book_id")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person", referencedColumnName = "person_id")
    private Person person;
    public void setPersonBookDateExpiration(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getPersonBookDate());
        calendar.add(Calendar.DATE, days);
        this.personBookDateExpiration = calendar.getTime();
    }
}
