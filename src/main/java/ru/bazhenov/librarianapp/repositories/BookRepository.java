
package ru.bazhenov.librarianapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bazhenov.librarianapp.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select * from book where books_count>:count except " +
            "select book.id, book.book_name, book.author, book.creation_year, book.books_count from book " +
            "join person_book pb on book.id = pb.book where person=:id", nativeQuery = true)
    List<Book> findAvailableBookForUser(@Param("count") long count, @Param("id") long id);

}