package com.theword.thedigitalword.model;

public class Book {

    public Book(){

    }

    public Book(String icon, String name, String english, String dir,String lang){
        this.bookIcon=icon;
        this.bookName=name;
        this.englishName =english;
        this.dir = dir;
        this.lang=lang;
    }

    private String bookIcon;
    private String bookName;
    private String englishName;
    private String dir;
    private String lang;

    public String getBookIcon() {
        return bookIcon;
    }

    public void setBookIcon(String bookIcon) {
        this.bookIcon = bookIcon;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String selected) {
        lang = selected;
    }
}
