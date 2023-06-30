package ru.bazhenov.librarianapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@MappedSuperclass
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
}
