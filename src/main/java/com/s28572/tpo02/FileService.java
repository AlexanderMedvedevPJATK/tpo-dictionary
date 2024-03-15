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
    private PrintWriter writer;
    private final String filePath;
    private final CaseProfile caseProfile;

    public FileService(@Value("${pl.edu.pja.tpo02.filename}") String filePath, CaseProfile caseProfile) {
        this.filePath = filePath;
        this.caseProfile = caseProfile;
    }

    @PostConstruct
    public void initialize() {
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateEntriesFromFile();
    }

    private void populateEntriesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.ready()) {
                String line = caseProfile.modify(br.readLine());
                String[] translations = line.split(";");
                EntryRepository.addEntry(new Entry(translations[0], translations[1], translations[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWord(Entry entry) {
        EntryRepository.addEntry(entry);
        writer.printf("%s;%s;%s\n", entry.en(), entry.de(), entry.pl());
    }

    public void closeWriter() {
        writer.close();
    }
}
