package com.s28572.tpo02;

import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Controller
public class FlashcardsController {

    private final FileService fileService;

    public FlashcardsController(FileService fileService) {
        this.fileService = fileService;
    }

    public int showOptions() {
        printOptions();
        int response = -1;
        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);

        while (!validInput) {
            try {
                response = scanner.nextInt();
                if (response >= 0 && response < 4) {
                    validInput = true;
                } else {
                    printOptions();
                }
            } catch (InputMismatchException e) {
                scanner.next();
                printOptions();
            }
        }

        return response;
    }

    public void printOptions() {
        System.out.println("Enter 1, 2 or 3 to choose one of the following options: ");
        System.out.println("1. Add a new word to the dictionary");
        System.out.println("2. Display all words in a dictionary");
        System.out.println("3. Create a test");
        System.out.println("0. Quit");
    }

    public void addWord() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the word you want to add in English: ");
        String en = scanner.next();
        System.out.print("Enter the translation in German: ");
        String de = scanner.next();
        System.out.print("Enter the translation in Polish: ");
        String pl = scanner.next();

        fileService.saveWord(new Entry(en, de, pl));
    }

    public void showAll() {
        EntryRepository.getEntries().forEach(System.out::println);
    }

    public void test() {
        Scanner scanner = new Scanner(System.in);
        List<Entry> entries = EntryRepository.getEntries();
        Entry word = entries.get((int) (Math.random() * entries.size()));

        System.out.printf("Enter English translation for \"%s\"\n", word.pl());
        String en = scanner.next();

        System.out.printf("Enter German translation for \"%s\"\n", word.pl());
        String de = scanner.next();

        if (!en.equalsIgnoreCase(word.en()) && !de.equalsIgnoreCase(word.de())) {
            System.out.println("You missed both :(");
            System.out.printf("%s in English is %s and in German is %s\n", word.pl(), word.en(), word.de());
            System.out.println("Better luck next time!");
        } else if (!en.equalsIgnoreCase(word.en())) {
            System.out.printf("""
                    Almost! You German translation is correct,
                    but %s in English is %s
                    """, word.pl(), word.en());
        } else if (!de.equalsIgnoreCase(word.de())) {
            System.out.printf("""
                    Close! You English translation is correct,
                    but %s in German is %s
                    """, word.pl(), word.de());
        } else {
            System.out.println("You are correct! Congratulations!");
        }
    }
}
