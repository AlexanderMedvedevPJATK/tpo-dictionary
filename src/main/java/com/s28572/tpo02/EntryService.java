package com.s28572.tpo02;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntryService {
    private final EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Transactional
    public void addEntry(Entry entry) {
        entryRepository.save(entry);
    }

    public Optional<Entry> findById(Long id) {
        return entryRepository.findById(id);
    }

    public List<Entry> getEntries() {
        return entryRepository.findAll();
    }

    public List<Entry> searchEntriesEnglish(String keyword) {
        return entryRepository.findByEnContainingIgnoreCase(keyword);
    }

    public List<Entry> searchEntriesGerman(String keyword) {
        return entryRepository.findByDeContainingIgnoreCase(keyword);
    }

    public List<Entry> searchEntriesPolish(String keyword) {
        return entryRepository.findByPlContainingIgnoreCase(keyword);
    }

    @Transactional
    public void delete(Long id) {
        findById(id).ifPresent(entryRepository::delete);
    }

    @Transactional
    public void modify(Entry modifiedEntry) {
        entryRepository.save(modifiedEntry);
    }
}
