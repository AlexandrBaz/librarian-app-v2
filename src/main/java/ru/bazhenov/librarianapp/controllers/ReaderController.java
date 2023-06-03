package ru.bazhenov.librarianapp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bazhenov.librarianapp.models.Reader;
import ru.bazhenov.librarianapp.service.ReaderService;
import ru.bazhenov.librarianapp.util.ReaderValidator;

@Controller
@RequestMapping("/reader")
public class ReaderController {

    private final ReaderService readerService;
    private final ReaderValidator readerValidator;
    @Autowired
    public ReaderController(ReaderService readerService, ReaderValidator readerValidator) {
        this.readerService = readerService;
        this.readerValidator = readerValidator;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("reader", readerService.index());
        return "reader/index";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("reader") Reader reader) {
        return "reader/new";
    }

    @PostMapping
    public String addNewReader(@ModelAttribute("reader") @Valid Reader reader, BindingResult bindingResult){
        readerValidator.validate(reader, bindingResult);
        if (bindingResult.hasErrors()) {
            return "reader/new";
        }
        readerService.addReader(reader);
        return "redirect:/reader";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("reader", readerService.getByIdReader(id));
        return "reader/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("reader") @Valid Reader reader, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "reader/edit";
        }
        readerService.updateReader(id, reader);
        return "redirect:/reader";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("reader", readerService.getByIdReader(id));

        return "reader/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        readerService.deleteReader(id);
        return "redirect:/reader";
    }


}