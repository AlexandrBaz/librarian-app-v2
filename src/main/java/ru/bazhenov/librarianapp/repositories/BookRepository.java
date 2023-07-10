
package ru.bazhenov.librarianapp.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.bazhenov.librarianapp.models.Book;

import java.util.List;

@Repository

public interface BookRepository extends JpaRepository<Book, Long> {

    @NonNull
    Page<Book> findAll(@NotNull Pageable pageable);

    @Query(value = "select * from book where books_count>:count except " +
            "select b.id, b.name, b.author, b.year, b.books_count from book b " +
            "join person_book pb on b.id = pb.book where person=:id", nativeQuery = true)
    List<Book> findAvailableBookForUser(@Param("count") long count, @Param("id") long id);

}