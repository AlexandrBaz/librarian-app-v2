package ru.bazhenov.librarianapp.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PersonBookDTO extends AbstractDTO{
    @Temporal(TemporalType.TIMESTAMP)
    private Date personBookDate;
    @Temporal(TemporalType.DATE)
    private Date personBookDateExpiration;
    private Boolean personBookIsExpired;
    private Long bookId;
    private Long personId;
    public void setPersonBookDateExpirationDTO(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getPersonBookDate());
        calendar.add(Calendar.DATE, days);
        this.personBookDateExpiration = calendar.getTime();
    }
}
