package org.example;

import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;

import java.nio.file.Paths;
import java.util.*;

import static org.example.PartOfSpeechTagger.detectPartOfSpeech;

public class Main {
    public static void main(String[] args) {
        try {
            String text = """
                    Мемка Мімка 2 вчить вчасно розпізнати, вірно зрозуміти, а часом і проконтролювати свої чи чужі емоції. Може бути як забавкою для дітей, так і хорошим інструментом для тренування комунікаційних навичок та вміння зважено приймати рішення.
                    """;
            Set<String> stopWords = StopWordsLoader.loadStopWords("C:\\Users\\maRko\\OneDrive\\Робочий стіл\\cross_lab_5\\src\\main\\resources\\ukrainian-stopwords.txt");

            List<String> tokens = TextCleaner.cleanAndTokenize(text, stopWords);

            Dictionary dictionary = Dictionary.read(Paths.get("C:\\Users\\maRko\\OneDrive\\Робочий стіл\\cross_lab_5\\src\\main\\resources\\ukrainian.dict"));
            DictionaryLookup lookup = new DictionaryLookup(dictionary);

            Map<String, Set<String>> posMap = new HashMap<>();
            Map<String, String> wordToPos = new HashMap<>();
            List<String> unknownWords = new ArrayList<>();

            for (String word : tokens) {
                List<WordData> results = lookup.lookup(word);

                if (results.isEmpty()) {
                    unknownWords.add(word);
                } else {
                    for (WordData result : results) {
                        String tag = result.getTag() != null ? result.getTag().toString() : "";
                        String description = MorphologicalTagTranslator.translateTag(tag);

                        String[] parts = description.split(", ");
                        for (String part : parts) {
                            posMap.computeIfAbsent(part, k -> new HashSet<>()).add(word);
                            wordToPos.put(word, part);
                        }

                        if (description.contains("дієслово")) {
                            posMap.computeIfAbsent("дієслово", k -> new HashSet<>()).add(word);
                            wordToPos.put(word, "дієслово");
                        }
                    }
                }
            }

            for (String word : unknownWords) {
                String pos = detectPartOfSpeech(word);
                if (pos != null) {
                    posMap.computeIfAbsent(pos, k -> new HashSet<>()).add(word);
                    wordToPos.put(word, pos);
                    System.out.println("Слово '" + word + "' автоматично визначено як: " + pos);
                } else {
                    System.out.println("Не вдалося визначити частину мови для слова: " + word);
                }
            }

            System.out.println("\nРезультати:");
            for (Map.Entry<String, Set<String>> entry : posMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            List<String> resultPatterns = new ArrayList<>();

            for (int i = 0; i < tokens.size(); i++) {
                String current = tokens.get(i);

                if (hasPOS(current, "дієслово", posMap) && i + 1 < tokens.size()) {
                    String next = tokens.get(i + 1);
                    if (hasPOS(next, "інфінітив", posMap)) {
                        resultPatterns.add(capitalize(current + " " + next));
                    }
                }

                if (hasPOS(current, "інфінітив", posMap)) {
                    StringBuilder phrase = new StringBuilder(current);
                    int j = i + 1;
                    while (j < tokens.size() && hasPOS(tokens.get(j), "прикметник", posMap)) {
                        phrase.append(" ").append(tokens.get(j));
                        j++;
                    }
                    if (j < tokens.size() && hasPOS(tokens.get(j), "іменник", posMap)) {
                        phrase.append(" ").append(tokens.get(j));
                        resultPatterns.add(phrase.toString());
                    }
                }

                if (hasPOS(current, "дієслово", posMap)) {
                    StringBuilder phrase = new StringBuilder(current);
                    int j = i + 1;
                    while (j < tokens.size() && hasPOS(tokens.get(j), "прикметник", posMap)) {
                        phrase.append(" ").append(tokens.get(j));
                        j++;
                    }
                    if (j < tokens.size() && hasPOS(tokens.get(j), "іменник", posMap)) {
                        phrase.append(" ").append(tokens.get(j));
                        resultPatterns.add(phrase.toString());
                    }
                }
            }

            Set<String> uniqueResults = new LinkedHashSet<>(resultPatterns);

            System.out.println("\nЗнайдені комбінації:");
            for (String pattern : uniqueResults) {
                System.out.println(pattern);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static boolean hasPOS(String word, String pos, Map<String, Set<String>> posMap) {
        Set<String> words = posMap.get(pos);
        return words != null && words.contains(word);
    }
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}