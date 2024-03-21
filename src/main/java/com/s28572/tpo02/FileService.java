package com.s28572.tpo02;

import com.s28572.tpo02.profiles.CaseProfile;
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
    private final EntryRepository entryRepository;

    public FileService(@Value("${pl.edu.pja.tpo02.filename}") String filePath, EntryRepository entryRepository) {
        this.filePath = filePath;
        this.entryRepository = entryRepository;
    }

    @PostConstruct
    private void populateEntriesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            List<Entry> existingEntries = entryRepository.getEntries();
            while (br.ready()) {
                String[] translations = br.readLine().split(";");
                Entry entry = new Entry(translations[0], translations[1], translations[2]);
                if (!existingEntries.contains(entry)) {
                    entryRepository.addEntry(entry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWord(String en, String de, String pl) {
        entryRepository.addEntry(new Entry(en, de, pl));
    }
}
