package ru.bazhenov.librarianapp.service;

import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.BookDto;

import java.util.List;
import java.util.Locale;

@Service
public class ManagerServiceImpl extends AbstractUserService implements ManagerService{

    public void addBook(BookDto bookDto) {
        getBookRepositoryService().addBook(getBookMapper().toEntity(bookDto));
    }

    public List<BookDto> searchBook(String key) {
        return getBookRepositoryService().getAllBooks("name").stream()
                .map(getBookMapper()::toDTO)
                .filter(bookDto -> bookDto.getName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT)))
                .toList();
    }
}
