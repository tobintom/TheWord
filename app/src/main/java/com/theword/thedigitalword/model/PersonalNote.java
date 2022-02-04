package com.theword.thedigitalword.model;

import java.io.Serializable;

public class PersonalNote implements Serializable {
    private int noteId;
    private String noteTitle;
    private String noteContent;
    private String timestamp;

    public PersonalNote(){
        super();
    }

    public PersonalNote(int id, String title, String content, String time){
        this.noteId=id;
        this.noteTitle = title;
        this.noteContent = content;
        this.timestamp = time;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
