package com.s28572.tpo02;

import com.s28572.tpo02.profiles.CaseProfile;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class FlashcardsController {

    private final FileService fileService;
    private final EntryRepository entryRepository;
    private final Scanner scanner;
    private final CaseProfile caseProfile;

    public FlashcardsController(FileService fileService, EntryRepository entryRepository, Scanner scanner, CaseProfile caseProfile) {
        this.fileService = fileService;
        this.entryRepository = entryRepository;
        this.scanner = scanner;
        this.caseProfile = caseProfile;
    }

    public int showOptions() {
        printOptions();
        int response = -1;
        boolean validInput = false;

        while (!validInput) {
            response = scanner.nextInt();
            if (response >= 0 && response < 6) {
                validInput = true;
            } else {
                printOptions();
            }
        }

        return response;
    }

    public void printOptions() {
        System.out.println("Choose one of the following options: ");
        System.out.println("1. Add a new word to the dictionary");
        System.out.println("2. Display all words in a dictionary");
        System.out.println("3. Create a test");
        System.out.println("4. Search");
        System.out.println("5. Delete");
        System.out.println("0. Quit");
    }

    public void addWord() {
        System.out.print("Enter the word you want to add in English: ");
        String en = scanner.next();
        System.out.print("Enter the translation in German: ");
        String de = scanner.next();
        System.out.print("Enter the translation in Polish: ");
        String pl = scanner.next();

        fileService.saveWord(en, de, pl);
    }

    public void showAll() {
        System.out.println("1. Default view");
        System.out.println("2. A -> Z sorted");
        System.out.println("3. Z -> A sorted");

        int sorting = scanner.nextInt();

        List<Entry> entries = entryRepository.getEntries();

        if (sorting == 2 || sorting == 3) {
            sortByLang(sorting, entries);
        } else {
            printWithCaseProfile(entries);
        }
    }

    private void sortByLang(int sorting, List<Entry> entries) {
        System.out.println("1. Sort English");
        System.out.println("2. Sort German");
        System.out.println("3. Sort Polish");

        int sortingLang = scanner.nextInt();
        List<Entry> sortedEntries = new ArrayList<>();
        if (sortingLang <= 3 && sortingLang >= 1) {
            switch (sortingLang) {
                case 1 -> sortedEntries = entries.stream().sorted(Comparator.comparing(Entry::getEn)).toList();
                case 2 -> sortedEntries = entries.stream().sorted(Comparator.comparing(Entry::getDe)).toList();
                case 3 -> sortedEntries = entries.stream().sorted(Comparator.comparing(Entry::getPl)).toList();
            }
        }

        switch (sorting) {
            case 2 -> printWithCaseProfile(sortedEntries);
            case 3 -> printWithCaseProfile(sortedEntries.reversed());
        }
    }

    public void test() {
        List<Entry> entries = entryRepository.getEntries();
        Entry word = caseProfile.modify(entries.get((int) (Math.random() * entries.size())));

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

    public void search() {
        System.out.println("Select the language you want to search by:");
        System.out.println("1. English");
        System.out.println("2. German");
        System.out.println("3. Polish");
        int lang = scanner.nextInt();
        if (lang < 1 || lang > 3) {
            System.out.println("Invalid input");
            return;
        }
        System.out.println("Enter search keyword: ");
        String keyword = scanner.next();

        List<Entry> resList = null;
        switch (lang) {
            case 1 -> resList = entryRepository.searchEntriesEnglish(keyword);
            case 2 -> resList = entryRepository.searchEntriesGerman(keyword);
            case 3 -> resList = entryRepository.searchEntriesPolish(keyword);
        }

        if (resList != null && !resList.isEmpty()) {
            System.out.println("--- SEARCH RESULT ---");
            printWithCaseProfile(resList);
        } else {
            System.out.println("Nothing found");
        }
    }

    public void flushScanner() {
        scanner.nextLine();
    }

    public void printWithCaseProfile(List<Entry> entries) {
        entries.stream().map(caseProfile::modify).forEach(System.out::println);
        System.out.println("---------------------");
    }
}
