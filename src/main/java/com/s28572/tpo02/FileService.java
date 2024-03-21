package com.s28572.tpo02;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;

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
        if (entryService.getEntries().isEmpty()) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                while (br.ready()) {
                    String[] translations = br.readLine().split(";");
                    entryService.addEntry(new Entry(translations[0], translations[1], translations[2]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveWord(String en, String de, String pl) {
        entryService.addEntry(new Entry(en, de, pl));
    }
}
