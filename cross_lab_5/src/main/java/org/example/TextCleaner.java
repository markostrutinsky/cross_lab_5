package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class TextCleaner {
    private static final Pattern VALID_WORD = Pattern.compile("^[а-щА-ЩЬьЮюЯяЇїІіЄєҐґ]+$");

    public static List<String> cleanAndTokenize(String text, Set<String> stopWords) {
        text = text.toLowerCase();
        text = text.replaceAll("[^а-щА-ЩЬьЮюЯяЇїІіЄєҐґ\\s]", " ");
        String[] words = text.split("\\s+");
        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (VALID_WORD.matcher(word).matches() && !stopWords.contains(word)) {
                result.add(word);
            }
        }
        return result;
    }
}
