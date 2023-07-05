package ru.bazhenov.librarianapp.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ru.bazhenov.librarianapp.models.PersonBook;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookDto extends AbstractDto {
    @NotEmpty(message = "Введите название книги")
    private String name;
    @NotEmpty(message = "Введите автора книги")
    private String author;
    @Pattern(regexp = "^[12][0-9]{3}$|^[12][0-9]{3}-[12][0-9]{3}$", message = "Введите год выпуска, формат year или year-year")
    private String year;
    private long booksCount;
    private List<PersonBook> personBookList;

    @Transient
    @Temporal(TemporalType.DATE)
    private Date bookDateTaken;
    @Transient
    @Temporal(TemporalType.DATE)
    private Date bookDateExpiration;
    @Transient
    private Boolean bookReturnIsExpired;

    public void setBookDateExpiration(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getBookDateTaken());
        calendar.add(Calendar.DATE, days);
        this.bookDateExpiration = calendar.getTime();
    }
}
