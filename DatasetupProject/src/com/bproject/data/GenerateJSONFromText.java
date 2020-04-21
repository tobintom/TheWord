package com.bproject.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONObject;

import com.google.gson.JsonObject;

/**
 * Generate from e-bible source
 * @author tobin
 *
 */
public class GenerateJSONFromText {

	public static void main(String[] args) {
		try{
			
			
			try (Writer writer = new OutputStreamWriter(
						    new FileOutputStream("X:\\EclipseWorkspace\\Datasetup\\dir\\UKR.json"), "UTF-8");					
					
		        ) {		            
				 	JSONObject obj = new JSONObject();
					displayDirectoryContents(new File("X:\\EclipseWorkspace\\Datasetup\\dir\\src"),obj,writer);
					writer.flush();            
		        }		
			
		}catch(Exception e){
			e.printStackTrace();			
		}
	}
	
	
	private static void displayDirectoryContents(File dir, JSONObject obj,Writer writer) {
		try {
			 
			File[] files = dir.listFiles();
			Arrays.sort(files, new Comparator<File>() {
	            @Override
	            public int compare(File o1, File o2) {
	               
	                return (o1.getName()).compareTo(o2.getName());
	            }

	         
	        });
			for (File file : files) {
				if (file.isDirectory()) {
					 
					displayDirectoryContents(file,obj,writer);
				} else {
					String type = "OT";
					String[] fileName = file.getName().split("_");
                    int i = Integer.parseInt(fileName[1]);
                    if(i<41) {
                    	i= i-1;
                    }else {
                    	i=i-30;
                    }
					
					if(i>39){
												
						type = "NT";
					}
					
					String book =  String.valueOf(i);
					book = book.length()<2?"0"+book:book;
					String chapter = String.valueOf(Integer.parseInt(fileName[3]));
					printBook(book, file, obj, writer,type,chapter);
					 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printBook(String book, File file,JSONObject objg,Writer writer, String type,String chapter){
		 String line;
		    
		    String verse = "";
		    String verseText = "";
		    int i=0;
		try{
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream(file), "UTF-8"))) {
			   
			    while ((line = br.readLine()) != null) {
			    	i++;
			    	if(i>2) {
			    		verse = String.valueOf(i-2);
			    		verseText = line;
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
			    	}
			    	
			    }        
			        
			}			       
			
		}catch(Exception e){
			System.err.println(book + "-" +chapter);
			e.printStackTrace();
			System.exit(0);
		}
	}
	 
}
