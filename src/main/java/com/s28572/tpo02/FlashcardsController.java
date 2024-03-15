package com.s28572.tpo02;

import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Controller
public class FlashcardsController {

    private final FileService fileService;
    private final EntryRepository entryRepository;
    private final Scanner scanner;

    public FlashcardsController(FileService fileService, EntryRepository entryRepository, Scanner scanner) {
        this.fileService = fileService;
        this.entryRepository = entryRepository;
        this.scanner = scanner;
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
        System.out.print("Enter the word you want to add in English: ");
        String en = scanner.next();
        System.out.print("Enter the translation in German: ");
        String de = scanner.next();
        System.out.print("Enter the translation in Polish: ");
        String pl = scanner.next();

        fileService.saveWord(new Entry(en, de, pl));
    }

    public void showAll() {
        System.out.println("1. Default view");
        System.out.println("2. A -> Z sorted");
        System.out.println("3. Z -> A sorted");

        int sorting = scanner.nextInt();

        switch (sorting) {
            case 1 -> entryRepository.getEntries().forEach(System.out::println);

            // TODO
//            case 2 -> entryRepository.getEntries().forEach(System.out::println);
//            case 3 -> entryRepository.getEntries().forEach(System.out::println);
        }
    }

    public void test() {
        List<Entry> entries = entryRepository.getEntries();
        Entry word = entries.get((int) (Math.random() * entries.size()));

        System.out.printf("Enter English translation for \"%s\"\n", word.getPl());
        String en = scanner.next();

        System.out.printf("Enter German translation for \"%s\"\n", word.getPl());
        String de = scanner.next();

        if (!en.equalsIgnoreCase(word.getEn()) && !de.equalsIgnoreCase(word.getDe())) {
            System.out.println("You missed both :(");
            System.out.printf("%s in English is %s and in German is %s\n", word.getPl(), word.getEn(), word.getDe());
            System.out.println("Better luck next time!");
        } else if (!en.equalsIgnoreCase(word.getEn())) {
            System.out.printf("""
                    Almost! You German translation is correct,
                    but %s in English is %s
                    """, word.getPl(), word.getEn());
        } else if (!de.equalsIgnoreCase(word.getDe())) {
            System.out.printf("""
                    Close! You English translation is correct,
                    but %s in German is %s
                    """, word.getPl(), word.getDe());
        } else {
            System.out.println("You are correct! Congratulations!");
        }
    }
}
