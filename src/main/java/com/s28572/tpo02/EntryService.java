package com.s28572.tpo02;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryService {
    private final EntityManager entityManager;

    public EntryService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void addEntry(Entry entry) {
        entityManager.persist(entry);
    }

    public Entry findById(Long id) {
        return entityManager.find(Entry.class, id);
    }

    public List<Entry> getEntries() {
        return entityManager.createQuery("SELECT e FROM Entry e", Entry.class).getResultList();
    }

    public List<Entry> searchEntriesEnglish(String keyword) {
        return entityManager.createQuery("SELECT e FROM Entry e WHERE LOWER(e.en) LIKE LOWER(:keyword)", Entry.class)
                .setParameter("keyword", "%" + keyword + "%")  // match everything containing keyword
                .getResultList();
    }

    public List<Entry> searchEntriesGerman(String keyword) {
        return entityManager.createQuery("SELECT e FROM Entry e WHERE LOWER(e.de) LIKE LOWER(:keyword)", Entry.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    public List<Entry> searchEntriesPolish(String keyword) {
        return entityManager.createQuery("SELECT e FROM Entry e WHERE LOWER(e.pl) LIKE LOWER(:keyword)", Entry.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    @Transactional
    public void delete(Long id) {
        entityManager.remove(findById(id));
    }

    @Transactional
    public void modify(Entry modifiedEntry) {
        entityManager.merge(modifiedEntry);
    }
}
