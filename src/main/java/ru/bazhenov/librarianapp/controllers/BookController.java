//package ru.bazhenov.librarianapp.controllers;
//
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import ru.bazhenov.librarianapp.models.Book;
//import ru.bazhenov.librarianapp.models.Reader;
//import ru.bazhenov.librarianapp.service.BookService;
//import ru.bazhenov.librarianapp.util.BookValidator;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/books")
//public class BookController {
//    private final BookService bookService;
//    private final BookValidator bookValidator;
//
//    @Autowired
//    public BookController(BookService bookService, BookValidator bookValidator) {
//        this.bookService = bookService;
//        this.bookValidator = bookValidator;
//    }
//
//    @GetMapping
//    public String index(Model model,
//                        @RequestParam(value = "page") Optional<Integer> offset,
//                        @RequestParam(value = "books_per_page") Optional<Integer> limit,
//                        @RequestParam(value = "sort_by_year") Optional<Boolean> sortSet) {
//        if (offset.isPresent() && limit.isPresent() && sortSet.isPresent()) {
//            model.addAttribute("paginationAndSort", bookService.paginationAndSort(offset.get(), limit.get()));
//        } else if (offset.isPresent() && limit.isPresent()) {
//            model.addAttribute("paginationAndSort", bookService.index(offset.get(), limit.get()));
//        } else if (sortSet.orElse(false)) {
//            model.addAttribute("book", bookService.indexSortByYear());
//        } else {
//            model.addAttribute("book", bookService.index());
//        }
//        return "books/index";
//    }
//
//    @GetMapping("/search")
//    public String search(Model model, @RequestParam(value = "search") Optional<String> search){
//        search.ifPresent(s -> model.addAttribute("book", bookService.search(s)));
//        return "books/search";
//    }
//
//    @GetMapping("/new")
//    public String newBook(@ModelAttribute("book") Book book) {
//        return "books/new";
//    }
//
//    @PostMapping
//    public String addNewBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
//        bookValidator.validate(book, bindingResult);
//        if (bindingResult.hasErrors()) {
//            return "books/new";
//        }
//        bookService.addNewBook(book);
//        return "redirect:/books";
//    }
//
//    @GetMapping("/{id}")
//    public String getBookById(@PathVariable("id") int id, Model model, @ModelAttribute("reader") Reader reader) {
//        model.addAttribute("bookAndReaders", bookService.getBookList(id));
//        return "books/show";
//    }
//
//    @PatchMapping("/reset-{id}")
//    public String resetReader(@PathVariable("id") int id) {
//        bookService.resetReader(id);
//        return "redirect:/books/{id}";
//    }
//
//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
//        if (bindingResult.hasErrors()) {
//            return "books/edit";
//        }
//        bookService.updateBook(id, book);
//        return "redirect:/books";
//    }
//
//    @GetMapping("{id}/edit")
//    public String edit(Model model, @PathVariable("id") int id) {
//        model.addAttribute("book", bookService.getBookById(id));
//        return "books/edit";
//    }
//
//    @PatchMapping("/set-reader{id}")
//    public String setReader(@ModelAttribute("reader") Reader reader, @PathVariable("id") int bookId) {
//        bookService.setBookReader(reader.getId(), bookId);
//        return "redirect:/books/{id}";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id) {
//        bookService.delete(id);
//        return "redirect:/books";
//    }
//}