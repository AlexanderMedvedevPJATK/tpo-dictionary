package com.s28572.tpo02;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    List<Entry> findByEnContainingIgnoreCase(String value);
    List<Entry> findByDeContainingIgnoreCase(String value);
    List<Entry> findByPlContainingIgnoreCase(String value);
}
