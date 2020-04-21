package com.theword.wordcontent.model;

import org.springframework.data.mongodb.core.mapping.Field;

public class Verse {
	
	@Field("verse")
	private String verse;
	
	@Field("text")
	private String text;
	 		
	public Verse() {
		super();
	}

	public String getVerse() {
		return verse;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}	
	
	public int getIntVerse() {
		return Integer.valueOf(getVerse());
	}
		
}
