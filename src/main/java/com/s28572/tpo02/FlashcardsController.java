package com.s28572.tpo02;

import com.s28572.tpo02.profiles.CaseProfile;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class FlashcardsController {

    private final FileService fileService;
    private final EntryService entryService;
    private final Scanner scanner;
    private final CaseProfile caseProfile;

    public FlashcardsController(FileService fileService, EntryService entryService, Scanner scanner, CaseProfile caseProfile) {
        this.fileService = fileService;
        this.entryService = entryService;
        this.scanner = scanner;
        this.caseProfile = caseProfile;
    }

    public int showOptions() {
        printOptions();
        int response = -1;
        boolean validInput = false;

        while (!validInput) {
            response = scanner.nextInt();
            if (response >= 0 && response < 7) {
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
        System.out.println("6. Modify");
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

    public List<Entry> showAll(boolean print) {
        System.out.println("1. Default view");
        System.out.println("2. A -> Z sorted");
        System.out.println("3. Z -> A sorted");

        int sorting = scanner.nextInt();

        List<Entry> entries = entryService.getEntries();

        if (sorting == 2 || sorting == 3) {
            entries = sortByLang(sorting, entries, print);
        } else if (print) {
            System.out.println("------ ENTRIES ------");
            printWithCaseProfile(entries);
        }
        return entries;
    }

    private List<Entry> sortByLang(int sorting, List<Entry> entries, boolean print) {
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
            case 2 -> {
                if (print) {
                    System.out.println("------ ENTRIES ------");
                    printWithCaseProfile(sortedEntries);
                }
                return sortedEntries;
            }
            case 3 -> {
                if (print) {
                    System.out.println("------ ENTRIES ------");
                    printWithCaseProfile(sortedEntries.reversed());
                }
                return sortedEntries.reversed();
            }
        }
        return null;
    }

    public void test() {
        List<Entry> entries = entryService.getEntries();
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

    public List<Entry> search(boolean print) {
        System.out.println("Select the language you want to search by:");
        System.out.println("1. English");
        System.out.println("2. German");
        System.out.println("3. Polish");
        int lang = scanner.nextInt();
        if (lang < 1 || lang > 3) {
            System.out.println("Invalid input");
            return null;
        }
        System.out.println("Enter search keyword: ");
        String keyword = scanner.next();

        List<Entry> resList = null;
        switch (lang) {
            case 1 -> resList = entryService.searchEntriesEnglish(keyword);
            case 2 -> resList = entryService.searchEntriesGerman(keyword);
            case 3 -> resList = entryService.searchEntriesPolish(keyword);
        }
        if (print) {
            if (resList != null && !resList.isEmpty()) {
                System.out.println("--- SEARCH RESULT ---");
                printWithCaseProfile(resList);
            } else {
                printNothingFound();
            }
        }
        return resList;
    }


    public void delete() {
        List<Entry> entries = listAllOrSearch();
        if (entries != null && !entries.isEmpty()) {
            printNumerated(entries);
            System.out.println("List numbers of entries to delete, separated by space: ");
            if (scanner.hasNextLine()) scanner.nextLine();
            String toDelete = scanner.nextLine();
            try {
                int[] indexes = Arrays.stream(toDelete.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
                for (int index : indexes) {
                    if (index > entries.size() || index < 0) {
                        System.out.println("---------------------");
                        System.out.println("Entry with index " + index + " doesnt exist and was skipped");
                        System.out.println("---------------------");
                    } else {
                        Entry entry = entries.get(index - 1);
                        entryService.delete(entry.getId());
                        System.out.println(" --- " + entry + " WAS DELETED ---");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("INVALID INPUT");
            }
        } else {
            printNothingFound();
        }
    }

    public void modify() {
        List<Entry> entries = listAllOrSearch();
        if (entries != null && !entries.isEmpty()) {
            printNumerated(entries);
            System.out.println("Enter the number of a single entry to modify: ");
            int number = scanner.nextInt();
            if (number < 0 || number > entries.size()) {
                System.out.println("INVALID ENTRY NUMBER");
            } else {
                Entry toModify = entryService.findById(entries.get(number - 1).getId()).orElseThrow(NoSuchElementException::new);
                System.out.println("What would you like to modify?");
                System.out.println("1. English");
                System.out.println("2. German");
                System.out.println("3. Polish");
                int lang = scanner.nextInt();

                if (lang < 1 || lang > 3) {
                    System.out.println("INVALID INPUT");
                    return;
                }
                System.out.println("New value: ");
                String newVal = scanner.next();
                switch (lang) {
                    case 1 -> toModify.setEn(newVal);
                    case 2 -> toModify.setDe(newVal);
                    case 3 -> toModify.setPl(newVal);
                }
                entryService.modify(toModify);
            }
        } else {
            printNothingFound();
        }

    }
    public void flushScanner() {
        scanner.nextLine();
    }

    public List<Entry> listAllOrSearch() {
        System.out.println("List all or search records?");
        System.out.println("1. List all");
        System.out.println("2. Search");
        int response = scanner.nextInt();
        List<Entry> entries = null;

        switch (response) {
            case 1 -> entries = showAll(false);
            case 2 -> entries = search(false);
        }
        return entries;
    }

    public void printWithCaseProfile(List<Entry> entries) {
        entries.stream().map(caseProfile::modify).forEach(System.out::println);
        System.out.println("---------------------");
    }

    public void printNumerated(List<Entry> entries) {
        System.out.println("------ ENTRIES ------");
        for (int i = 0; i < entries.size(); i++) {
            System.out.println(i + 1 + ". " + caseProfile.modify(entries.get(i)));
        }
        System.out.println("---------------------");
    }

    public void printNothingFound() {
        System.out.println("---------------------");
        System.out.println("Nothing found!");
        System.out.println("---------------------");
    }
}
