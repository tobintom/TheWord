package com.theword.wordmeta.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Document(collection="BLANG")
public class Bible {
	
	@Id
	private ObjectId _id;
	
	@Field("code")
	private String bId;
	
	@Field("language")
	private String language;
	
	@Field("dir")
	private String dir;
	
	private List<Book> books;
	
	public Bible() {
		super();
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getbId() {
		return bId;
	}

	public void setbId(String bId) {
		this.bId = bId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public List<Book> getBooks() {
		if(books!=null) {
			Collections.sort(
					books, Comparator.comparing(Book::getBookCode));
		}
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}		

}
