package com.theword.thedigitalword.model;

import java.io.Serializable;

public class BibleDBContent implements Serializable {

    private int noteId;
    private String title;
    private String bibleBook;
    private String bibleChapter;
    private String bibleVerse;
    private String bibleNote;
    private String color;
    private String timestamp;
    private String link;

    public BibleDBContent(){
        super();
    }

    public BibleDBContent(int noteId, String title, String bibleBook, String bibleChapter, String bibleVerse, String bibleNote, String color, String timestamp,String link) {
        this.noteId = noteId;
        this.title = title;
        this.bibleBook = bibleBook;
        this.bibleChapter = bibleChapter;
        this.bibleVerse = bibleVerse;
        this.bibleNote = bibleNote;
        this.color = color;
        this.timestamp = timestamp;
        this.link = link;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getBibleBook() {
        return bibleBook;
    }

    public void setBibleBook(String bibleBook) {
        this.bibleBook = bibleBook;
    }

    public String getBibleChapter() {
        return bibleChapter;
    }

    public void setBibleChapter(String bibleChapter) {
        this.bibleChapter = bibleChapter;
    }

    public String getBibleVerse() {
        return bibleVerse;
    }

    public void setBibleVerse(String bibleVerse) {
        this.bibleVerse = bibleVerse;
    }

    public String getBibleNote() {
        return bibleNote;
    }

    public void setBibleNote(String bibleNote) {
        this.bibleNote = bibleNote;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
