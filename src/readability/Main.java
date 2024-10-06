package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        String filename = args[0];

        try {
            analyzeReadability(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    // CREATED METHODS
    private static void analyzeReadability(String filename) throws IOException {
        // transfer file to text in one line
        Path filePath = Paths.get(filename);
        List<String> lines = Files.readAllLines(filePath);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
        }
        String text = sb.toString();


        // COUNT ALL PARAMETERS
        // count words
        String[] words = text.split("\\s+");
        int totalWords = words.length;

        // count sentences
        int totalSentences = text.split("[.!?]").length;

        // count character
        String textWithoutSpaces = text.replace(" ", "");
        int totalChars = textWithoutSpaces.length();

        // count syllables
        int totalSyllables = getTotalSyllables(text, totalWords, totalSentences);

        // count polysyllables
        int totalPolySyllables = getTotalPolySyllables(words);

        // CALCULATIONS
        // Automated readability index
        double ariScore =
                4.71 * ((double) totalChars / totalWords) + 0.5 * ((double) totalWords / totalSentences) - 21.43;

        // Flesch-Kincaid readability test
        double fleshKincaidScore =
                0.39 * ((double) totalWords / totalSentences) + 11.8 * ((double) totalSyllables / totalWords) - 15.59;

        // SMOG index
        double smogScore = 1.043 * Math.sqrt(totalPolySyllables * ((double) 30 / totalSentences)) + 3.1291;

        // Coleman-Liau index
        double L = getL(totalChars, totalWords);
        double S = getS(totalSentences, totalWords);
        double colemanScore = 0.0588 * L - 0.296 * S - 15.8;


        // GET AGES FROM SCORES
        int ariAge = getAge((int) Math.ceil(ariScore));
        int fleschKincaidAge = getAge((int) Math.ceil(fleshKincaidScore));
        int smogAge = getAge((int) Math.ceil(smogScore));
        int colemanAge = getAge((int) Math.ceil(colemanScore));

        double avgAge = (double) (ariAge + fleschKincaidAge + smogAge + colemanAge) / 4;


        // PRINT RESULTS
        System.out.printf("""
                    The text is:\s
                    %s

                    Words: %d
                    Sentences: %d
                    Characters: %d
                    Syllables: %d
                    Polysyllables: %d
                    Enter the score you want to calculate (ARI, FK, SMOG, CL, all): all
                    
                    Automated Readability Index: %.2f (about %d-year-olds).
                    Flesch–Kincaid readability tests: %.2f (about %d-year-olds).
                    Simple Measure of Gobbledygook: %.2f (about %d-year-olds).
                    Coleman–Liau index: %.2f (about %d-year-olds).

                    This text should be understood in average by %.2f-year-olds.""",
                text, totalWords, totalSentences, totalChars, totalSyllables, totalPolySyllables, ariScore, ariAge, fleshKincaidScore, fleschKincaidAge, smogScore, smogAge, colemanScore, colemanAge, avgAge);

    }

    private static int getTotalSyllables(String text, int totalWords, int totalSentences) {

        return text
                .replaceAll("e\\b", "")
                .replaceAll("you", "a")
                .replaceAll("[aeiouy]{2}", "a")
                .replaceAll(",", "")
                .replaceAll("[AEIOUY]", "a")
                .replaceAll(" th ", " a ")
                .replaceAll("[0-9]+", "a")
                .replaceAll("[^aeiou]", "").length();
    }

    private static int getTotalPolySyllables(String[] words) {
        int totalPolySyllables = 0;
        String vowels = "aeiouy";

        for (String word : words) {
            word = word
                    .replaceAll("e\\b", "")
                    .replaceAll("[aeiouy]{2}", "a");

            int count = 0;
            for (int i = 0; i < word.length(); i++) {
                if (vowels.indexOf(word.charAt(i)) != -1) count++;
            }
            if (count >= 3) {
                totalPolySyllables++;
            }
        }
        return totalPolySyllables;
    }

    private static double getS(int totalSentences, int totalWords) {
        return (double) totalSentences / totalWords * 100;
    }

    private static double getL(int totalChars, int totalWords) {
        return (double) totalChars / totalWords * 100;
    }

    private static int getAge(int roundedScore) {
        int age = 0;

        switch (roundedScore) {
            case 1 -> age = 6;
            case 2 -> age = 7;
            case 3 -> age = 8;
            case 4 -> age = 9;
            case 5 -> age = 10;
            case 6 -> age = 11;
            case 7 -> age = 12;
            case 8 -> age = 13;
            case 9 -> age = 14;
            case 10 -> age = 15;
            case 11 -> age = 16;
            case 12 -> age = 17;
            case 13 -> age = 18;
            case 14 -> age = 22;
        }
        return age;
    }
}


