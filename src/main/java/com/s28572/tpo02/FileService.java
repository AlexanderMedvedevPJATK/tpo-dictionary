package com.s28572.tpo02;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
@PropertySource("classpath:external.properties")
public class FileService {
    private final String filePath;
    private final EntryService entryService;

    public FileService(@Value("${pl.edu.pja.tpo02.filename}") String filePath, EntryService entryService) {
        this.filePath = filePath;
        this.entryService = entryService;
    }

    @PostConstruct
    private void populateEntriesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            List<Entry> existingEntries = entryService.getEntries();
            while (br.ready()) {
                String[] translations = br.readLine().split(";");
                Entry entry = new Entry(translations[0], translations[1], translations[2]);
                if (!existingEntries.contains(entry)) {
                    entryService.addEntry(entry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWord(String en, String de, String pl) {
        entryService.addEntry(new Entry(en, de, pl));
    }
}
