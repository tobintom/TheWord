package com.theword.thedigitalword.model;

public class SearchModel {

    private String bookName;
    private String bookNumber;
    private String englishName;
    private String chapter;
    private String content;
    private String direction;
    private String id;
    private String query;

    public SearchModel(String query, String bookName, String bookNumber, String englishName, String chapter, String content, String direction, String id) {
        this.bookName = bookName;
        this.bookNumber = bookNumber;
        this.englishName = englishName;
        this.chapter = chapter;
        this.content = content;
        this.id = id;
        this.query = query;
        this.direction = direction;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getChapter() {
        return chapter;
    }

    public String getContent() {
        return content;
    }

    public String getDirection() {
        return direction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
