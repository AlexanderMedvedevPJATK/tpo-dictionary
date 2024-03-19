package com.s28572.tpo02;

import com.s28572.tpo02.profiles.CaseProfile;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@PropertySource("classpath:external.properties")
public class FileService {
    private final String filePath;
    private final CaseProfile caseProfile;
    private final EntryRepository entryRepository;

    public FileService(@Value("${pl.edu.pja.tpo02.filename}") String filePath, CaseProfile caseProfile, EntryRepository entryRepository) {
        this.filePath = filePath;
        this.caseProfile = caseProfile;
        this.entryRepository = entryRepository;
    }

    @PostConstruct
    private void populateEntriesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.ready()) {
                String line = caseProfile.modify(br.readLine());
                String[] translations = line.split(";");
                entryRepository.addEntry(new Entry(translations[0], translations[1], translations[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWord(String en, String de, String pl) {
        en = caseProfile.modify(en);
        de = caseProfile.modify(de);
        pl = caseProfile.modify(pl);
        Entry entry = new Entry(en, de, pl);
        entryRepository.addEntry(entry);
    }
}
