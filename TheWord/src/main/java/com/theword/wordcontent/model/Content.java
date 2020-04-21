package com.theword.wordcontent.model;
 
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

public class Content {
	
	private String id;
	@Field("language")
	private String language;
	private String dir;
	
	@Field("code")
	private String bookCode;
	
	@Field("value")
	private String bookName;

	private String chapter; 

	List<Verse> verses;
	
	public Content() {
		super();
	}
 
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getBookCode() {
		return bookCode;
	}

	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
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

	public List<Verse> getVerses() {
		if(verses!=null) {
			Collections.sort(verses,
					Comparator.comparing(Verse::getIntVerse));
		}
		return verses;
	}

	public void setVerses(List<Verse> verses) {
		this.verses = verses;
	}
	
}
