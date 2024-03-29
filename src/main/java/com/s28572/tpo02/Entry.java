package com.s28572.tpo02;

import jakarta.persistence.*;

@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String en;
    private String de;
    private String pl;

    public Entry() {
    }

    public Entry(String en, String de, String pl) {
        this.en = en;
        this.de = de;
        this.pl = pl;
    }

    public Long getId() {
        return id;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    @Override
    public String toString() {
        return String.format("English: %s, German: %s, Polish: %s", en, de, pl);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Entry entry = (Entry) obj;

        return this.en.equalsIgnoreCase(entry.en) &&
                this.de.equalsIgnoreCase(entry.de) &&
                this.pl.equalsIgnoreCase(entry.pl);
    }
}
