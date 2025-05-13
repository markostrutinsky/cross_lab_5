package org.example;

public class MorphologicalTagTranslator {

    public static String translateTag(String tag) {
        if (isGerund(tag)) {
            return "дієприслівник";
        } else if (isParticiple(tag)) {
            return "дієприкметник";
        } else if (tag.contains("verb")) {
            return "дієслово" + getVerbForm(tag);
        } else if (tag.contains("noun")) {
            return "іменник";
        } else if (tag.contains("adj")) {
            return "прикметник";
        } else if (tag.contains("adv")) {
            return "прислівник";
        } else {
            return "невідомий тег: " + tag;
        }
    }

    private static boolean isGerund(String tag) {
        return tag.contains("ger") || tag.contains("verb:gerund");
    }

    private static boolean isParticiple(String tag) {
        return tag.contains("part") || tag.contains("adjp") || (tag.contains("adj") && tag.contains("verb"));
    }

    private static String getVerbForm(String tag) {
        StringBuilder result = new StringBuilder(", ");

        // Вид дієслова
        if (tag.contains("imperf")) {
            result.append("недоконаний вид");
        } else if (tag.contains("perf")) {
            result.append("доконаний вид");
        }

        // Інфінітив
        if (tag.contains("inf")) {
            result.append(", інфінітив");
        }

        // Рефлексивність (за умови, що бібліотека це маркує)
        if (tag.contains("refl") || tag.contains("reflexive")) {
            result.append(", зворотне (рефлексивне)");
        }

        return result.toString();
    }
}

