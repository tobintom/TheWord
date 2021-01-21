package com.theword.wordmeta.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.theword.wordmeta.model.Bible;
import com.theword.wordmeta.model.Book;

@Component
public class DataComponent {

	@Autowired
	BibleRepository _bibleRepository;
	
	static List<Bible> bibles = null;
	static Bible engBible = null;
	static Map<String, String> engMap;
	
	@PostConstruct
	public void init() {
		//Initialize on application start up
		//Get all available Bibles
		bibles = _bibleRepository.findAll(Sort.by(Direction.ASC, "language"));
	
		//Get English Books
		 engBible = _bibleRepository.getBibleBooks("ENG");
		 engMap = new HashMap<String, String>();
		 for(Book b: engBible.getBooks()) {
			  engMap.put(b.getBookCode(), b.getBookName());
		  }
	}
	
	public Bible getEnglishBible() {
		return engBible;
	}
	
	public List<Bible> getBibles(){
		return bibles;
	}
	
	public Map<String, String> getEnglishBooks(){
		return engMap;
	}
}
