package com.bproject.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class GenerateFromTxt {
	
	public static void main(String[] args) {
		try{
			
			
			try (Writer writer = new OutputStreamWriter(
						    new FileOutputStream("X:\\EclipseWorkspace\\Datasetup\\DATA\\ENG.csv"), "UTF-8");					
		            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat .DEFAULT
		                    .withHeader("book", "type", "chapter", "verse","text"));
		        ) {		            
				
					ReadContents(new File("X:\\EclipseWorkspace\\Datasetup\\DATA\\kjv.txt"),csvPrinter);
				    csvPrinter.flush();            
		        }		
			 	
		}catch(Exception e){
			e.printStackTrace();			
		}
	}
	
	private static void ReadContents(File file,CSVPrinter csvPrinter){
		 String line;
		    String chapter = "";
		    String verse = "";
		    String verseText = "";
		try{
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream(file), "UTF-8"))) {
				
			    while ((line = br.readLine()) != null) {
			    	if(line!=null && line.length()>0){
			    		 
			    		String[] contents = line.split("\\|\\|");
			    		 
			    		String book = contents[0].substring(0, 2);
			    		String type = contents[0].substring(2) + "T";
			    	    chapter = contents[1];
			    		verse = contents[2];
			    		verseText=contents[3]; 
			    		csvPrinter.printRecord(book, type, chapter, verse,verseText);
			    		book = "";
			    		type = "";
			    		chapter = "";
			    		verse = "";
			    		verseText = "";
			    	}			    	
			    }
			     
			}			       
			
		}catch(Exception e){			 
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	

}
