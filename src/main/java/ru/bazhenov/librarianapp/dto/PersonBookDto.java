package ru.bazhenov.librarianapp.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PersonBookDto extends AbstractDto {
    @Temporal(TemporalType.DATE)
    private LocalDate personBookDate;
    private Long bookId;
    private Long personId;
    private LocalDate personBookDateExpiration;
    private Boolean personBookIsExpired;
    @Transient
    private long totalDaysExpiration;


}
