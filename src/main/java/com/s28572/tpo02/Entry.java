package com.s28572.tpo02;

public record Entry(String en, String de, String pl) {

    @Override
    public String toString() {
        return String.format("English: %s, German: %s, Polish: %s", en, de, pl);
    }
}
