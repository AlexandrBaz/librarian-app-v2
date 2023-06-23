
package ru.bazhenov.librarianapp.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.bazhenov.librarianapp.models.Book;

import java.util.List;

@Repository

public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findAllByBooksCountGreaterThan(Long count);
    @NonNull
    Page<Book> findAll(@NotNull Pageable pageable);
    List<Book> findAllByNameContainsIgnoreCase(String search);
}
