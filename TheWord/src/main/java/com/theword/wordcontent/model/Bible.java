package com.theword.wordcontent.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

}
