package com.s28572.tpo02;

import java.util.ArrayList;
import java.util.List;
public class EntryRepository {
    private static final List<Entry> entries = new ArrayList<>();

    private EntryRepository() {
    }

    public static void addEntry(Entry entry) {
        entries.add(entry);
    }

    public static List<Entry> getEntries() {
        return entries;
    }
}
