package ru.bazhenov.librarianapp.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class PersonBookDTO {
    private long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date personBookDate;
    @Temporal(TemporalType.DATE)
    private Date personBookDateExpiration;
    private Boolean personBookIsExpired;
    private BookDTO book;
    private PersonDTO person;
    public void setPersonBookDateExpirationDTO(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getPersonBookDate());
        calendar.add(Calendar.DATE, days);
        this.personBookDateExpiration = calendar.getTime();
    }
}
