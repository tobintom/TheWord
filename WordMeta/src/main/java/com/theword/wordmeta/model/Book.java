package com.theword.wordmeta.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="B_BOOK")
public class Book {
	
	@Id
	private ObjectId _id;
	
	@Field("code")
	private String bookCode;
	
	@Field("language")
	private String language;
	
	@Field("value")
	private String bookName;

	private String chapter;
	
	private String englishName;
	
	private List<String> chapters;
	
	private List<String> verses;
	
	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getBookCode() {
		return bookCode;
	}

	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public List<String> getChapters() {
		return chapters;
	}

	public void setChapters(List<String> chapters) {
		this.chapters = chapters;
	}

	public List<String> getVerses() {
		return verses;
	}

	public void setVerses(List<String> verses) {
		this.verses = verses;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	
	
	
}
