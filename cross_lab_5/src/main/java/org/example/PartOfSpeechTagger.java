package org.example;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PartOfSpeechTagger {
    private static final Map<String, Pattern> posPatterns = new LinkedHashMap<>();

    static {
        posPatterns.put("іменник", Pattern.compile("(а|ев|ов|е|ями|ами|еи|и|ей|ой|ий|й|иям|ям|ием|ем|ам|ом|о|у|ах|иях|ях|ь|ию|ью|ю|ия|ья|я|і|ові|ї|ею|єю|ою|є|еві|єм|ем|ів|їв|ꞌю)$"));
        posPatterns.put("інфінітив", Pattern.compile("(ти|учи|ячи|вши|ши|ати|яти|ючи)$"));
        posPatterns.put("доконаний вид", Pattern.compile("(ив|ивши|ившись)$"));
        posPatterns.put("зворотний", Pattern.compile("с[яьи]$"));
        posPatterns.put("прикметник", Pattern.compile("(ими|ій|ий|а|е|ова|ове|ів|є|їй|єє|еє|я|ім|ем|им|іх|их|ою|йми|іми|у|ю|ого|ому|ої)$"));
        posPatterns.put("дієприкметник", Pattern.compile("(ий|ого|ому|им|ім|а|ій|у|ою|ій|і|их|йми|их)$"));
        posPatterns.put("дієслово", Pattern.compile("(сь|ся|ив|ать|ять|у|ю|ав|али|учи|ячи|вши|ши|е|ме|ати|яти|є)$"));
    }

    public static String detectPartOfSpeech(String word) {
        for (Map.Entry<String, Pattern> entry : posPatterns.entrySet()) {
            if (entry.getValue().matcher(word).find()) {
                return entry.getKey();
            }
        }
        return "невизначено";
    }

    public static Map<String, String> tagWords(List<String> words) {
        Map<String, String> result = new LinkedHashMap<>();
        for (String word : words) {
            result.put(word, detectPartOfSpeech(word));
        }
        return result;
    }
}
