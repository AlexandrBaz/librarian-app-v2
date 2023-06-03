package ru.bazhenov.librarianapp.models;

import lombok.Data;
import org.springframework.data.domain.Page;
@Data
public class PaginationAndSort {
    int numberOfPage;
    int currentPage;
    Page<Book> bookPage;
}
