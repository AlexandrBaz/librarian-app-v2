package ru.bazhenov.librarianapp.util;

import org.springframework.stereotype.Component;
import ru.bazhenov.librarianapp.dto.BookDTO;
import ru.bazhenov.librarianapp.dto.PersonBookDTO;
import ru.bazhenov.librarianapp.dto.PersonDTO;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Person;
import ru.bazhenov.librarianapp.models.PersonBook;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OthersUtils {

    public Boolean isCyrillic(String text) {
        String cleanUpText = text.replaceAll("\\s|-|\\d+|", "").trim();
        for (int i = 0; i < cleanUpText.length(); i++) {
            if (!Character.UnicodeBlock.of(cleanUpText.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return false;
            }
        }
        return true;
    }
}
