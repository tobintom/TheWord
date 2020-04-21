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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.helper.StringUtil;

public class GenerateFromTextContent {

	public static void main(String[] args) {
		try{
			
			
			try (Writer writer = new OutputStreamWriter(
						    new FileOutputStream("X:\\EclipseWorkspace\\Datasetup\\dir\\ENG.csv"), "UTF-8");					
		            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
		                    .withHeader("book", "type", "chapter", "verse","text"));
		        ) {		            
				
					displayDirectoryContents(new File("X:\\EclipseWorkspace\\Datasetup\\dir\\en"),csvPrinter);
				    csvPrinter.flush();            
		        }		
			
		}catch(Exception e){
			e.printStackTrace();			
		}
	}
	
	
	private static void displayDirectoryContents(File dir, CSVPrinter csvPrinter) {
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
					 
					displayDirectoryContents(file,csvPrinter);
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
					printBook(book, file,csvPrinter,type,chapter);
					 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printBook(String book, File file,CSVPrinter csvPrinter, String type,String chapter){
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
			    	csvPrinter.printRecord(book, type, chapter, verse,verseText);
			    	}
			    	
			    }        
			        
			}			       
			
		}catch(Exception e){
			System.err.println(book + "-" +chapter);
			e.printStackTrace();
			System.exit(0);
		}
	}
	 
	private static String extractVerseText(String rawVerse,String verse,String book,String chapter){
		 
		String returnVerse = "";
		try{
			String textToRemove = "";
			int length = (verse+ " ").length();
			if(rawVerse !=null && rawVerse.trim().length()>5){
			returnVerse = (rawVerse.substring(rawVerse.indexOf(verse)+length));	
			if(returnVerse.contains("\\f") && returnVerse.contains("\\f*")){
				
				do{
					textToRemove = returnVerse.substring(returnVerse.indexOf("\\f"), returnVerse.indexOf("\\f*")+3);		
					returnVerse = (returnVerse.replace(textToRemove, " "));
					
				}while(returnVerse.contains("\\f") && returnVerse.contains("\\f*"));			
			
			}else if(returnVerse.contains("\\x") && returnVerse.contains("\\x*")){
				
				do{
					textToRemove = returnVerse.substring(returnVerse.indexOf("\\x"), returnVerse.indexOf("\\x*")+3);		
					returnVerse = (returnVerse.replace(textToRemove, " "));
					
				}while(returnVerse.contains("\\x") && returnVerse.contains("\\x*"));			
			
			}else if(returnVerse.contains("(") && returnVerse.contains(")")){
				//System.out.println("Encountered at " +book +" -"+chapter+"-" +verse +" - "+rawVerse);
				//textToRemove = returnVerse.substring(returnVerse.indexOf("("), returnVerse.lastIndexOf(")")+1);		
				//returnVerse = (returnVerse.replace(textToRemove, ""));
				//returnVerse = returnVerse.replaceAll("\\*", "");
			}else if(returnVerse.contains("\\qs") && returnVerse.contains("\\qs*")){
				
				do{
					textToRemove = returnVerse.substring(returnVerse.indexOf("\\qs"), returnVerse.indexOf("\\qs*")+4);		
					returnVerse = (returnVerse.replace(textToRemove, " "));
					
				}while(returnVerse.contains("\\qs") && returnVerse.contains("\\qs*"));			
			
			}
			}else{
				System.out.println(book +" - " +chapter + "-" + verse +": does not have text");
			}
		}catch(Exception e){
			System.err.println(verse);
			System.err.println(rawVerse);
			e.printStackTrace();
			throw e;
		}
		return returnVerse;
		
	}
	
	private static String extractText(String rawVerse,String verse,String book,String chapter){
		 
		String returnVerse = "";
		try{
			String textToRemove = "";
			 
			returnVerse =  rawVerse;	
			if(returnVerse.contains("\\f") && returnVerse.contains("\\f*")){
				
				do{
					textToRemove = returnVerse.substring(returnVerse.indexOf("\\f"), returnVerse.indexOf("\\f*")+3);		
					returnVerse = (returnVerse.replace(textToRemove, " "));
					
				}while(returnVerse.contains("\\f") && returnVerse.contains("\\f*"));			
			
			}else if(returnVerse.contains("\\x") && returnVerse.contains("\\x*")){
				
				do{
					textToRemove = returnVerse.substring(returnVerse.indexOf("\\x"), returnVerse.indexOf("\\x*")+3);		
					returnVerse = (returnVerse.replace(textToRemove, " "));
					
				}while(returnVerse.contains("\\x") && returnVerse.contains("\\x*"));			
			
			}else if(returnVerse.contains("(") && returnVerse.contains(")")){
				//System.out.println("Encountered at " +book +" -"+chapter+"-" +verse +" - "+rawVerse);
//				textToRemove = returnVerse.substring(returnVerse.indexOf("("), returnVerse.lastIndexOf(")")+1);		
//				returnVerse = (returnVerse.replace(textToRemove, ""));
//				returnVerse = returnVerse.replaceAll("\\*", "");
			}else if(returnVerse.contains("\\qs") && returnVerse.contains("\\qs*")){
				
				do{
					textToRemove = returnVerse.substring(returnVerse.indexOf("\\qs"), returnVerse.indexOf("\\qs*")+4);		
					returnVerse = (returnVerse.replace(textToRemove, " "));
					
				}while(returnVerse.contains("\\qs") && returnVerse.contains("\\qs*"));			
			
			}
		}catch(Exception e){
			 
			System.err.println(rawVerse);
			e.printStackTrace();
			throw e;
		}
		return returnVerse;
		
	}
}

