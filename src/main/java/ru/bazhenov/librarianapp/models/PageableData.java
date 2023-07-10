package ru.bazhenov.librarianapp.models;

import lombok.Data;
import ru.bazhenov.librarianapp.dto.BookDto;

import java.util.List;

@Data
public class PageableData {
    private int currenPage;
    private int pageSize;
    private String sort;
    List<BookDto> pageableList;
}