package com.bproject.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class BibleBooks {

	public static void main(String[] args) {
		try { String link ="https://www.bible.com/json/bible/books/";
		String[] books =	{"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"};
		String[] languages = {"ENG-1","AFK-2","ALB-7","AMH-1260","ARB-67","ASM-1979","BEN-1681","BUL-1443","BUR-2397","CHS-41","CHT-139","CRO-2475","CZH-509","DAN-49","DUT-328","EST-1","FAR-181","FIL-177","FIN-330","FRH-21","GER-2170","GRK-921","GUJ-1911","HEB-2220","HIN-1980","HUN-84","ICE-2359","IND-306","ITL-141","JAP-1820","KAN-1898","KAZ-1","KOR-88","LIT-310","MAL-1912","MAO-94","MAR-1686","MON-369","NEP-1483","NOR-29","ORI-1886","POL-132","POR-215","PRB-212","PUN-1","ROM-191","RUS-313","SER-2165","SIN-1794","SPN-53","SWA-74","SWE-154","TAM-339","TEL-1895","THA-174","TUR-170","UKR-186","URU-1","VET-193","XHO-281","ZUL-286" };
		try (Writer writer = new OutputStreamWriter(
			    new FileOutputStream("X:\\EclipseWorkspace\\Datasetup\\dir\\B_BOOK.json"), "UTF-8");					
		
    ) {		            
	 	 
		 
			
	 	for(String lang:languages) {
	 	
	 	String[] breakd = lang.split("-");
	 	String name = breakd[0];
	 	String langCode = breakd[1];
	 	
	 	String urlLink = link+langCode;
	 	URL url = new URL(urlLink);
	 	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.addRequestProperty("User-Agent", "Mozilla");
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
		conn.setRequestProperty("Accept", "application/json");

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream()),"UTF-8"));

			String output;
			String result = "";
			System.out.println("Output from Server .... \n");
			int i=0;
			while ((output = br.readLine()) != null) {
				result +=output;
			}
			
			JSONObject obj = new JSONObject(result);
			JSONArray array = obj.getJSONArray("items");
			for (int j = 0; j < array.length(); j++)
			{
			    String book = array.getJSONObject(j).getString("human");
			    i++;
				JSONObject compactJson = new JSONObject();  
				compactJson.put("code", (String.valueOf(i).length()<2?"0"+i:String.valueOf(i)));
				compactJson.put("language", name);
				compactJson.put("value", book); 
		    	
		    	
		    	writer.write(compactJson.toString());
				writer.write("\n");
			

			}	

			conn.disconnect();	 			
	 	}
		writer.flush();            
    }		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
