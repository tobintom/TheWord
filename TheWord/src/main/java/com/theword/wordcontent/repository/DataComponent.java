package com.theword.wordcontent.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.theword.wordcontent.model.Book;
import com.theword.wordcontent.model.Content;
 

@Component
public class DataComponent {

	@Autowired
	ContentRepository _bibleRepository;
	
	static List<Content> bibles = null;
	static List<Book> engBible = null;
	static Map<String, String> engMap;
	static NavigableMap<String, Integer> bibleChapters = null;
	
	@PostConstruct
	public void init() {
		//Initialize on application start up
		//Get all available Bibles
		bibles = _bibleRepository.findAll(Sort.by(Direction.ASC, "language"));
	
		//Get English Books
		 engBible = _bibleRepository.getBibleBooks("ENG");
		 engMap = new HashMap<String, String>();
		 for(Book b: engBible) {
			  engMap.put(b.getBookCode(), b.getBookName());
		  }
		 //init chapters
		 initBibleChapters();
	}
	
	private void initBibleChapters() {
		bibleChapters = new TreeMap<String, Integer>();
		bibleChapters.put("01",50);		
		bibleChapters.put("02",40);
		bibleChapters.put("03",27);
		bibleChapters.put("04",36);
		bibleChapters.put("05",34);
		bibleChapters.put("06",24);
		bibleChapters.put("07",21);
		bibleChapters.put("08",4);
		bibleChapters.put("09",31);
		bibleChapters.put("10",24);
		bibleChapters.put("11",22);
		bibleChapters.put("12",25);
		bibleChapters.put("13",29);
		bibleChapters.put("14",36);
		bibleChapters.put("15",10);
		bibleChapters.put("16",13);
		bibleChapters.put("17",10);
		bibleChapters.put("18",42);
		bibleChapters.put("19",150);
		bibleChapters.put("20",31);
		bibleChapters.put("21",12);
		bibleChapters.put("22",8);
		bibleChapters.put("23",66);
		bibleChapters.put("24",52);
		bibleChapters.put("25",5);
		bibleChapters.put("26",48);
		bibleChapters.put("27",12);
		bibleChapters.put("28",14);
		bibleChapters.put("29",3);
		bibleChapters.put("30",9);
		bibleChapters.put("31",1);
		bibleChapters.put("32",4);
		bibleChapters.put("33",7);
		bibleChapters.put("34",3);
		bibleChapters.put("35",3);
		bibleChapters.put("36",3);
		bibleChapters.put("37",2);
		bibleChapters.put("38",14);
		bibleChapters.put("39",4);
		bibleChapters.put("40",28);
		bibleChapters.put("41",16);
		bibleChapters.put("42",24);
		bibleChapters.put("43",21);
		bibleChapters.put("44",28);
		bibleChapters.put("45",16);
		bibleChapters.put("46",16);
		bibleChapters.put("47",13);
		bibleChapters.put("48",6);
		bibleChapters.put("49",6);
		bibleChapters.put("50",4);
		bibleChapters.put("51",4);
		bibleChapters.put("52",5);
		bibleChapters.put("53",3);
		bibleChapters.put("54",6);
		bibleChapters.put("55",4);
		bibleChapters.put("56",3);
		bibleChapters.put("57",1);
		bibleChapters.put("58",13);
		bibleChapters.put("59",5);
		bibleChapters.put("60",5);
		bibleChapters.put("61",3);
		bibleChapters.put("62",5);
		bibleChapters.put("63",1);
		bibleChapters.put("64",1);
		bibleChapters.put("65",1);
		bibleChapters.put("66",22);
	}
	
	public NavigableMap<String, Integer> getBibleChapters(){
		return bibleChapters;
	}
	
	public List<Book> getEnglishBible() {
		return engBible;
	}
	
	public List<Content> getBibles(){
		return bibles;
	}
	
	public Map<String, String> getEnglishBooks(){
		return engMap;
	}
}
