package ru.bazhenov.librarianapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
//@RequiredArgsConstructor
public class Person extends AbstractEntity{
    @NotEmpty(message = "Введите Фамилию Имя Отчество")
    @Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "Формат ввода, с большой буквы, разделенный пробелами")
    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;
    @NotEmpty(message = "Введите логин")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{4,}$", message = "Формат ввода - латиница, цифры, точки, тире и подчеркивания. Длинна не менее 4")
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    @NotEmpty(message = "Введите пароль")
    @Column(name = "password", nullable = false)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Формат ввода - латиница, не менее 8 символов\n" +
            "\n" +
            "Содержит хотя бы одну цифру\n" +
            "\n" +
            "Содержит по крайней мере один нижний альфа-символ и один верхний альфа-символ\n" +
            "\n" +
            "Содержит по крайней мере один символ в наборе специальных символов (@#%$^ и т.д.)\n" +
            "\n" +
            "Не содержит пробелов, табуляции и т.д.")
    private String password;
    @NotEmpty(message = "Введите Ваш email")
    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;
    @Column(name = "year_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date yearOfBirth;
    @OneToMany(mappedBy = "person", targetEntity = PersonBook.class, cascade = CascadeType.PERSIST)
    private List<PersonBook> personBookList;
    @Enumerated(EnumType.STRING)
    @Column(name = "person_role", nullable = false)
    private PersonRole personRole;
    @Column(name = "is_baned", nullable = false)
    private Boolean isBanned;
    @Column(name = "pass_expired")
    private Boolean passIsExpired;
}
