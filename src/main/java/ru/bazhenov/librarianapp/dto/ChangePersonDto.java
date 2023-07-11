package ru.bazhenov.librarianapp.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangePersonDto extends AbstractDto{

    @NotEmpty(message = "Введите Фамилию Имя Отчество")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "Формат ввода, с большой буквы, разделенный пробелами")
    private String fullName;
    private String login;
    //    @NotEmpty(message = "Введите пароль")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Формат ввода - латиница, не менее 8 символов\n" +
//            "\n" +
//            "Содержит хотя бы одну цифру\n" +
//            "\n" +
//            "Содержит по крайней мере один нижний альфа-символ и один верхний альфа-символ\n" +
//            "\n" +
//            "Содержит по крайней мере один символ в наборе специальных символов (@#%$^ и т.д.)\n" +
//            "\n" +
//            "Не содержит пробелов, табуляции и т.д.")
    private String password;
    private String newPassword;
    @NotEmpty(message = "Введите Ваш email")
    @Email
    private String email;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date yearOfBirth;
    private List<PersonBookDto> personBookList;
    private PersonRole personRole;
    private Boolean isBanned;
    private Boolean passIsExpired;

}
