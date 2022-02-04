package com.bproject.data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RandomPopularVerse {
	public static void main(String[] args) {
		try {
			String[] books =	{"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"};
			Map<String, String> bookMap= new HashMap<String, String>();
			
			int m =0;
			for(String book:books) {
				m++;
				bookMap.put(book,(m<10)?"0"+m:String.valueOf(m));				 
			}	
		String line;	
		ArrayList<String> vlist = new ArrayList<String>();
		File input = new File("C:\\Users\\tobin\\git\\theword\\DatasetupProject\\DATA\\randomVerses.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
			    new FileInputStream(input), "UTF-8"))) {
		   
		    while ((line = br.readLine()) != null) {
		    	vlist.add(line);
		    }
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		SimpleDateFormat formatter=new SimpleDateFormat("MM-dd-yyyy");
	      GregorianCalendar cal=new GregorianCalendar();
	      int year=2020;
	      int total=365;
	      cal.set(Calendar.YEAR, year);
	      if (cal.isLeapYear(year)) {
	         total++;
	      }
	      
	      try (Writer writer1 = new OutputStreamWriter(
				    new FileOutputStream("C:\\Users\\tobin\\git\\theword\\DatasetupProject\\DATA\\RANDOM_DAILY_VERSE.json"), "UTF-8")) {
  	
	    	  int s =vlist.size()/2;
		      for(int d=1; d<=total; d++) {
		         cal.set(Calendar.DAY_OF_YEAR, d);
		         Date date1=cal.getTime();
		         //System.out.println(d+" "+ (date.getMonth()+1)+"/"+date.getDate() + " = " +vlist.get(d-1) +","+vlist.get((s-1)+d)); 
		      
			String month = String.valueOf(date1.getMonth()+1);
			month=month.length()<2?"0"+month:month;
			String date = String.valueOf(date1.getDate());
			date=date.length()<2?"0"+date:date;
			List<String> vfinalList = new ArrayList<String>();
			vfinalList.add(vlist.get(d-1));
			vfinalList.add(vlist.get((s-1)+d));
			JSONObject compactJson = new JSONObject();  
			compactJson.put("month", (month));
			compactJson.put("date", date);					
			JSONArray array = new JSONArray();
			array.put(vfinalList);
			compactJson.put("references", vfinalList);
			
		      
	    	try {
	    		writer1.write(compactJson.toString());
	    		writer1.write("\n");
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
		

		      }
			
	      
	     
		}catch(Exception  e) {
			e.printStackTrace();
		}
		}catch(Exception e) {
			
		}
	}
}
	


