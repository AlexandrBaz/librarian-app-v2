package ru.bazhenov.librarianapp.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.BookDto;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.PageableData;
import ru.bazhenov.librarianapp.service.ManagerService;
import ru.bazhenov.librarianapp.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class Pagination {
    private final UserService userService;
    private final ManagerService managerService;

    public Pagination(UserService userService, ManagerService managerService) {
        this.userService = userService;
        this.managerService = managerService;
    }

    public Page<BookDto> getPaginatedPage(PageableData pageableData, PersonDto userDto) {
        Pageable pageable = PageRequest.of(pageableData.getCurrenPage() - 1, pageableData.getPageSize());
        List<BookDto> bookDtoList = userService.getListEnableToTake(userDto);
        pageableData.setPageableList(bookDtoList);
        sortList(pageableData);
        return createPage(pageable, pageableData);
    }

    public Page<BookDto> getPaginatedPage(PageableData pageableData) {
        Pageable pageable = PageRequest.of(pageableData.getCurrenPage() - 1, pageableData.getPageSize());
        List<BookDto> bookDtoList = managerService.getAllBooks();
        pageableData.setPageableList(bookDtoList);
        sortList(pageableData);
        return createPage(pageable, pageableData);
    }

    private void sortList(PageableData pageableData) {
        List<BookDto> outPutList = new ArrayList<>(pageableData.getPageableList());
        switch (pageableData.getSort()) {
            case ("year") -> outPutList.sort(Comparator.comparing(BookDto::getYear));
            case ("yearDesc") -> outPutList.sort((a, b) -> b.getYear().compareTo(a.getYear()));
            case ("author") -> outPutList.sort(Comparator.comparing(BookDto::getAuthor));
            case ("authorDesc") -> outPutList.sort((a, b) -> b.getAuthor().compareTo(a.getAuthor()));
            case ("count") -> outPutList.sort(Comparator.comparingLong(BookDto::getBooksCount));
            case ("countDesc") -> outPutList.sort((a, b) -> Long.compare(b.getBooksCount(), a.getBooksCount()));
            case ("nameDesc") -> outPutList.sort((a, b) -> b.getName().compareTo(a.getName()));
            default -> outPutList.sort(Comparator.comparing(BookDto::getName));
        }
        pageableData.setPageableList(outPutList);
    }

    private Page<BookDto> createPage(Pageable pageable, PageableData pageableData) {
        List<BookDto> listBookOnPage;
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        if (pageableData.getPageableList().size() < startItem) {
            listBookOnPage = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, pageableData.getPageableList().size());
            listBookOnPage = pageableData.getPageableList().subList(startItem, toIndex);
        }
        return new PageImpl<>(listBookOnPage, PageRequest.of(currentPage, pageSize), pageableData.getPageableList().size());
    }

    public List<Integer> getPageNumbers(int totalPages) {
        return IntStream.range(1, totalPages)
                .boxed()
                .toList();
    }
}
