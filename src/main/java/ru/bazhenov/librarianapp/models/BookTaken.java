package ru.bazhenov.librarianapp.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "book_taken")
@Data
public class BookTaken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "book_taken_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date BookTakenDate;

    @Transient
    @Temporal(TemporalType.DATE)
    private Date bookTakenDateExpiration;

    @Transient
    private Boolean bookTakenIsExpired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "id")
    private UserProfile userProfile;

    public void setBookTakenDateExpiration(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getBookTakenDate());
        calendar.add(Calendar.DATE, days);
        this.bookTakenDateExpiration = calendar.getTime();
    }
}
