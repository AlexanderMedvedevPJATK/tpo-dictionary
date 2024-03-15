package com.s28572.tpo02;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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

    // TODO
    public List<Entry> getEntries() {
        return entityManager.createQuery("SELECT e FROM Entry e", Entry.class).getResultList();
    }
}
