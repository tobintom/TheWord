package com.bproject.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;


import org.json.JSONObject;

/**
 * Generate from getBibles github source
 * @author tobin
 *
 */
public class GenerateJSONFromFlatFile {

	public static void main(String[] args) {
		try{
			
			
			try (Writer writer = new OutputStreamWriter(
						    new FileOutputStream("X:\\EclipseWorkspace\\Datasetup\\dir\\SPN.json"), "UTF-8");					
					
		        ) {		            
				 	JSONObject obj = new JSONObject();
					displayDirectoryContents(new File("X:\\EclipseWorkspace\\Datasetup\\dir\\src.txt"),obj,writer);
					writer.flush();            
		        }		
			
		}catch(Exception e){
			e.printStackTrace();			
		}
	}
	
	private static void displayDirectoryContents(File file, JSONObject obj,Writer writer) {
		try {
			 	String line;			    
			    String verse = "";
			    String verseText = "";			    
			    String book = "";
			    String chapter = "";
			    String type = "";
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream(file), "UTF-8"))) {
			   
			    while ((line = br.readLine()) != null) {
			    	//System.out.println(line);
			    	String[] contents = line.split("\\|\\|");
			    	  if(contents[0]!=null && contents[0].matches("^[0-9]{2}[a-zA-Z]{1}$")) {
			    	  book = contents[0].substring(0, 2);
		    		  type = contents[0].substring(2) + "T";
			    	  }else {
			    		  book = contents[0];
			    		  book = (book.length()<2?"0"+book:book);
			    		  if(Integer.parseInt(book)>39) {
			    			  type = "NT";
			    		  	}else{
			    		  		type = "OT";
						}
			    	  }
		    	    chapter = contents[1];
		    		verse = contents[2];
		    		verseText=contents[3]; 
			    	
		    		JSONObject compactJson = new JSONObject();
		    		compactJson.put("book", book);
		    		compactJson.put("type", type);
		    		compactJson.put("chapter", chapter);
		    		compactJson.put("verse", verse);
		    		compactJson.put("text", verseText);
		    		if(verseText==null || verseText.trim().length()<5) {
		    			System.err.println("NO TEXT IN "+book +" - "+chapter +" - "+verse);
		    		}
		    		//String compactJson = "{\"book\":\""+book+"\",\"type\":\""+type+"\",\"chapter\":\""+chapter+"\",\"verse\":\""+verse+"\",\"text\":\""+verseText+"\"}\n";

		    		
		    		
		    		writer.write(compactJson.toString());
		    		writer.write("\n");
		    		book = "";
		    		type = "";
		    		chapter = "";
		    		verse = "";
		    		verseText = "";
			    }
			}
		}catch(Exception e) {
		
			e.printStackTrace();
		}	 
	}

}
