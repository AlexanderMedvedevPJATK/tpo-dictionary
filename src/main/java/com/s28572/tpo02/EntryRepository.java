package com.s28572.tpo02;

import com.s28572.tpo02.profiles.CaseProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EntryRepository {
    private final EntityManager entityManager;

    public EntryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void addEntry(Entry entry) {
        entityManager.persist(entry);
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
}
