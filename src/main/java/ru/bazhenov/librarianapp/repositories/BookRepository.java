
package ru.bazhenov.librarianapp.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.bazhenov.librarianapp.models.Book;

import java.util.List;

@Repository

public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findAllByBooksCountGreaterThan(Long count, Sort by);
    @NonNull
    Page<Book> findAll(@NotNull Pageable pageable);
    List<Book> findAllByNameContainsIgnoreCase(String search);

//    @Transactional
//    @Query(nativeQuery = true, value = "select * from book as b left outer join book_taken bt on b.book_id = bt.id_book where user_profile_id is null or not user_profile_id =:id")
//    List<Book> findAllBookForUserById(long id);
//
//    @Transactional
//    @Query(nativeQuery = true , value = "select * from book as b left join book_taken bt on b.book_id = bt.id_book where user_profile_id = :id")
//    Page<Book> findAllAvailableBookByUserId(long id, Pageable pageable);

}

//select * from book as b left outer join book_taken bt on b.book_id = bt.id_book where user_profile_id IS NULL or not user_profile_id='1';