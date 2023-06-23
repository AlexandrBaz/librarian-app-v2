package ru.bazhenov.librarianapp.util;

import org.springframework.stereotype.Component;

@Component
public class OthersUtils {

    public Boolean isCyrillic(String text) {
        String cleanUpText = text.replaceAll("\\s|-|\\d+|","").trim();
        for(int i = 0; i < cleanUpText.length(); i++) {
            if(!Character.UnicodeBlock.of(cleanUpText.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return false;
            }
        }
        return true;
    }
}
