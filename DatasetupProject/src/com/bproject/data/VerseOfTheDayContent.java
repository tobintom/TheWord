package com.bproject.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VerseOfTheDayContent {

	public static void main(String[] args) {
		try { 
		
			String[] books =	{"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalm", "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"};
			Map<String, String> bookMap= new HashMap<String, String>();
			
			int m =0;
			for(String book:books) {
				m++;
				bookMap.put(book,(m<10)?"0"+m:String.valueOf(m));				 
			}
			
			int year =2016;
			ArrayList<String> dates = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
	        for (int month = 1; month <= 12; month++) {
	            calendar.set(Calendar.YEAR, year);
	            calendar.set(Calendar.MONTH, month-1); // since 0 = January
	            int maxDaysInMonth = calendar.getActualMaximum(Calendar.DATE);

	            for (int day = 1; day <= maxDaysInMonth; day++) {
	                // format date in such a way, that month and days are padded with zeroes up to 2 digits
	                String dateAsString = String.format("%d-%02d-%02d", year, month, day);
	                dates.add(dateAsString);
	            }
	        }
	        
	        String line;
			int i = 0;
			Map<String, List<String>> verseMap = new TreeMap<String, List<String>>();
	        
	        try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream("C:\\Users\\tobin\\git\\theword\\DatasetupProject\\DATA\\VERSE_DAY.txt"), "UTF-8"))) {			   
			    while ((line = br.readLine()) != null) {
			    	++i;
			    	String[] names = line.split("\\s+");
			    	String[] dateA = names[0].split("/");
			    	String mon = (dateA[0].length()<2)?"0"+dateA[0]:(dateA[0]); 
			    	String day =		dateA[1].length()<2?"0"+dateA[1]:dateA[1];
			    	String date = mon+"/"+day;
			    	//System.out.println(date);			    	
			    	String verse = line.split(names[0])[1];
			    	
			    	String[] verses = verse.split("\\;");
			    	String finalVerse = "";
			    	for(String ver:verses) {
			    		String[] verseParts = ver.trim().split("\\s+");
			    		String book ="";
			    		String rest = "";
			    		if(verseParts.length>2) {
			    			book = bookMap.get(verseParts[0] + " " +verseParts[1]);
			    			rest = verseParts[2].trim();
			    		}
			    		else {
			    			book = bookMap.get(verseParts[0]);
			    			rest = verseParts[1].trim();
			    		}
			    		 
			    		if(finalVerse.length()>0)
			    			finalVerse = finalVerse + ";";
			    		finalVerse = finalVerse + book+" "+rest;
			    	}
			    	
			    	//System.out.println(finalVerse);
			    	
			    	
			    	
			    	//System.out.println(verse);
			    	ArrayList<String> l = new ArrayList<String>();
			    	l.add(finalVerse);
			    	verseMap.put(date.trim(),l);		    	
			    }
	        }
	        
	        try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream("C:\\Users\\tobin\\git\\theword\\DatasetupProject\\DATA\\VERSE2.txt"), "UTF-8"))) {			   
			    while ((line = br.readLine()) != null) {
			    	++i;
			    	String[] names = line.split("\\|\\|");
			    	
			    	String[] dateA = names[0].split("-");
			    	String mon = dateA[1]; 
			    	String day = dateA[2];
			    	String date = mon+"/"+day;
			    	//System.out.println(date);			    	
			    	String verse = names[1];
			    	String[] verses = verse.split("\\;");
			    	String finalVerse = "";
			    	for(String ver:verses) {
			    		String[] verseParts = ver.trim().split("\\s+");
			    		String book ="";
			    		String rest = "";
			    		if(verseParts.length>2) {
			    			book = bookMap.get(verseParts[0] + " " +verseParts[1]);
			    			rest = verseParts[2].trim();
			    		}
			    		else {
			    			book = bookMap.get(verseParts[0]);
			    			rest = verseParts[1].trim();
			    		}
			    		 
			    		if(finalVerse.length()>0)
			    			finalVerse = finalVerse + ";";
			    		finalVerse = finalVerse + book+" "+rest;
			    	}
			    	
			    	//System.out.println(verse);
			    	verseMap.get(date.trim()).add(finalVerse);			    			    	
			    }
	        }

	        verseMap.forEach((k,v)->{
	        	System.out.println(k +" - "+v);
	        });
	        
	        try (Writer writer1 = new OutputStreamWriter(
						    new FileOutputStream("C:\\Users\\tobin\\git\\theword\\DatasetupProject\\DATA\\DAILY_VERSE.json"), "UTF-8")) {
	        	
	        	verseMap.forEach((k,v)->
				{	
					String month = k.split("/")[0];
					String date = k.split("/")[1];
					
					JSONObject compactJson = new JSONObject();  
					compactJson.put("month", (month));
					compactJson.put("date", date);					
					JSONArray array = new JSONArray();
					array.put(v);
					compactJson.put("references", v);
					
					
			    	try {
			    		writer1.write(compactJson.toString());
			    		writer1.write("\n");
			    	}catch(Exception e) {
			    		e.printStackTrace();
			    	}
				

					
					
					System.out.println(k+":"+v);
				});
	        	
	        }
			
	        
	        
	        
	        
	        
			
//	        for(String date:dates) {
//	        	
//	        	String urlLink = "https://www.christianity.com/bible/dbv.php?d=" + date + "&ver=kjv";
//	        	URL url = new URL(urlLink);
//	    	 	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//	    		conn.setRequestMethod("GET");
//	    		conn.addRequestProperty("User-Agent", "Mozilla");
//	            conn.setReadTimeout(5000);
//	            conn.setConnectTimeout(5000);
//	    		conn.setRequestProperty("Accept", "text/html");
//
//	    		BufferedReader br = new BufferedReader(new InputStreamReader(
//	    				(conn.getInputStream()),"UTF-8"));
//    
//	    			String output;
//	    			String result = "";
//
//	    			
//	    			while ((output = br.readLine()) != null) {
//	    				result +=output;
//	    			}
//	    			//System.out.println(result);
//	    			Document doc = Jsoup.parse(result,"UTF-8");
//	    			Elements elements = doc.select("div.col-md-12 >h2 >a");
//	    			for(Element e:elements){
//	    				String text = e.html();
//	    				String[] verses = text.split("<");
//	    				String verse = verses[0];
//	    				System.out.println(date+" || " + verses[0]);
//	    			}
//	    			
//	    			conn.disconnect();
//	        }	
//			
		
			
	        
			
			
//		String link ="https://www.bible.com/json/bible/books/";
//		String[] books =	{"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"};
//		String[] languages = {"ENG-1","AFK-2","ALB-7","AMH-1260","ARB-67","ASM-1979","BEN-1681","BUL-1443","BUR-2397","CHS-41","CHT-139","CRO-2475","CZH-509","DAN-49","DUT-328","EST-1","FAR-181","FIL-177","FIN-330","FRH-21","GER-2170","GRK-921","GUJ-1911","HEB-2220","HIN-1980","HUN-84","ICE-2359","IND-306","ITL-141","JAP-1820","KAN-1898","KAZ-1","KOR-88","LIT-310","MAL-1912","MAO-94","MAR-1686","MON-369","NEP-1483","NOR-29","ORI-1886","POL-132","POR-215","PRB-212","PUN-1","ROM-191","RUS-313","SER-2165","SIN-1794","SPN-53","SWA-74","SWE-154","TAM-339","TEL-1895","THA-174","TUR-170","UKR-186","URU-1","VET-193","XHO-281","ZUL-286" };
//		try (Writer writer = new OutputStreamWriter(
//			    new FileOutputStream("X:\\EclipseWorkspace\\Datasetup\\dir\\B_BOOK.json"), "UTF-8");					
//		
//				) {		            
//	 	 
//		 
//			
//	 	for(String lang:languages) {
//	 	
//	 	String[] breakd = lang.split("-");
//	 	String name = breakd[0];
//	 	String langCode = breakd[1];
//	 	
//	 	String urlLink = link+langCode;
//	 	URL url = new URL(urlLink);
//	 	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//		conn.setRequestMethod("GET");
//		conn.addRequestProperty("User-Agent", "Mozilla");
//        conn.setReadTimeout(5000);
//        conn.setConnectTimeout(5000);
//		conn.setRequestProperty("Accept", "application/json");
//
//		BufferedReader br = new BufferedReader(new InputStreamReader(
//				(conn.getInputStream()),"UTF-8"));
//
//			String output;
//			String result = "";
//			System.out.println("Output from Server .... \n");
//			int i=0;
//			while ((output = br.readLine()) != null) {
//				result +=output;
//			}
//			
////			JSONObject obj = new JSONObject(result);
////			JSONArray array = obj.getJSONArray("items");
////			for (int j = 0; j < array.length(); j++)
////			{
////			    String book = array.getJSONObject(j).getString("human");
////			    i++;
////				JSONObject compactJson = new JSONObject();  
////				compactJson.put("code", (String.valueOf(i).length()<2?"0"+i:String.valueOf(i)));
////				compactJson.put("language", name);
////				compactJson.put("value", book); 
////		    	
////		    	
////		    	//writer.write(compactJson.toString());
////				//writer.write("\n");
////			
////
////			}	
//
//			conn.disconnect();	 			
//	 	}
//		//writer.flush();            
//    }		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
