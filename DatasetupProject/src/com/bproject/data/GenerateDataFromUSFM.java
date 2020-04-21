
package com.bproject.data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GenerateDataFromUSFM {

	public static void main(String[] args) {
		try{
			
			
			try (Writer writer = new OutputStreamWriter(
						    new FileOutputStream("C:\\EclipseWorkspace\\TestAsyncClient\\dir\\PUN.csv"), "UTF-8");					
		            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat .DEFAULT
		                    .withHeader("book", "type", "chapter", "verse","text"));
		        ) {		            
				
					displayDirectoryContents(new File("C:\\EclipseWorkspace\\TestAsyncClient\\dir\\usfm"),csvPrinter);
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
	                int n1 = extractNumber(o1.getName());
	                int n2 = extractNumber(o2.getName());
	                return n1 - n2;
	            }

	            private int extractNumber(String name) {
	                int i = 0;
	                try {
	                     
	                    int e = name.indexOf('_');
	                    String number = name.substring(0, e);
	                    i = Integer.parseInt(number);
	                } catch(Exception e) {
	                    i = 0; // if filename does not match the format
	                           // then default to 0
	                }
	                return i;
	            }
	        });
			for (File file : files) {
				if (file.isDirectory()) {
					 
					displayDirectoryContents(file,csvPrinter);
				} else {
					String type = "OT";
					String fileName = file.getName();
                    String book = fileName.substring(0, fileName.indexOf("_"));
					int  i = Integer.parseInt(book);
					if(i>39){
						i = i-1;
						book = String.valueOf(i);
						type = "NT";
					}
					
					printBook(book, file,csvPrinter,type);
					 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printBook(String book, File file,CSVPrinter csvPrinter, String type){
		 String line;
		    String chapter = "";
		    String verse = "";
		    String verseText = "";
		try{
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream(file), "UTF-8"))) {
			   
			    while ((line = br.readLine()) != null) {			        
			        
			    	if(line.startsWith("\\c") && !line.startsWith("\\cl")){
			    		if(verseText!=null && verseText.length()>0){
				        	csvPrinter.printRecord(book, type, chapter, verse,verseText);
				        	verse = "";
				        	verseText = "";
			        	}
			        	chapter = line.substring(line.indexOf("\\c")+3).trim();
			        	continue;
			        }
			    	
			        if(line.startsWith("\\v")){
			        	if(verseText!=null && verseText.length()>0){
				        	csvPrinter.printRecord(book, type, chapter, verse,verseText);
				        	verse = "";
				        	verseText = "";
			        	}
			        	
			        	Pattern pdouble = Pattern.compile("\\d+-\\d+");
			        	Pattern p = Pattern.compile("\\d+");
						 Matcher mdouble = pdouble.matcher(line);
						 if(mdouble.find() && line.indexOf(mdouble.group())<9) {
							 System.out.println(verseText);
							 verse = mdouble.group();		             
					      }else{	
					    	  Matcher m = p.matcher(line);
					    	  if(m.find()) {
					    		  verse = m.group();		             
					    	  }		
					        }
			            verseText = extractVerseText(line, verse,book,chapter);        
			        	
			        }        
			        
			        if((line.startsWith("\\p")||line.startsWith("\\q1")||line.startsWith("\\q")
			        		||line.startsWith("\\q2")||line.startsWith("\\m"))&& (!line.startsWith("\\qa"))
			        				&& (!line.startsWith("\\mt")&& (!line.startsWith("\\ms")))){
			        	if(line.length()>3){
			        	String text = extractText(line.substring(3).trim(),verse,book,chapter);
			        	verseText = verseText +" " +text;
			        	continue;
			        	}
			        }
			    }
			    if(verseText!=null && verseText.length()>0){
		        	csvPrinter.printRecord(book, type, chapter, verse,verseText);
		        	verse = "";
		        	verseText = "";
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
