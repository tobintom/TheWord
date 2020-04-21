package com.bproject.data;

import java.io.File;
import java.io.FileOutputStream;

import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GenerateData {

	public static void main(String[] args) {
		try{
			
			
			try (Writer writer = new OutputStreamWriter(
						    new FileOutputStream("X:\\EclipseWorkspace\\Datasetup\\dir\\JAP.csv"), "UTF-8");					
		            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat .DEFAULT
		                    .withHeader("book", "type", "chapter", "verse","text"));
		        ) {		            
				
					displayDirectoryContents(new File("X:\\EclipseWorkspace\\Datasetup\\dir\\jp"),null,csvPrinter);
				    csvPrinter.flush();            
		        }		
			
		}catch(Exception e){
			e.printStackTrace();			
		}
	}
	
	
	private static void displayDirectoryContents(File dir, String chap,CSVPrinter csvPrinter) {
		try {
			String book = chap;
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
	                     
	                    int e = name.lastIndexOf('.');
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
					 
					displayDirectoryContents(file,file.getName(),csvPrinter);
				} else {
					if(book!=null)
					printBook(book, file,csvPrinter);
					 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printBook(String book, File file,CSVPrinter csvPrinter){
		try{
			
			String type = "";
			String filename=file.getName();
			
			String chapter = filename.substring(0, filename.indexOf("."));
			int bookNum = Integer.parseInt(book);
			if(bookNum>39){
				type = "NT";
			}else{
				type = "OT";
			}
				
				
			Document doc = Jsoup.parse(file,"UTF-8");
			
			Elements elements = doc.select("div.textBody >p");
			 
			for(Element e:elements){
				String text = e.html();
				//System.out.println(text);
				String[] verses = text.split("<br>");
				
				for(String verse:verses){
					if(verse!=null && verse.trim().length()>0){
						String verseText = (verse.substring(verse.indexOf("</span>")+7));
						verseText=verseText.replaceAll("<span class=\"person\">", "").replaceAll("<span class=\"place\">", "").replaceAll("</span>", "")
								.replaceAll("<span class=\"word\">", "").replaceAll("\"", "");
						//System.out.println(verseText);
					//System.out.println(book+"-"+chapter +"-"+verse);
					//System.out.println(verse.substring(verse.indexOf(">")+1,verse.indexOf("</span>")));
					if(verseText==null || verseText.trim().length()<5){
						System.err.println("NO VERSE TEXT FOR " + book +" - "+chapter +" - "+ verse);
					}
					csvPrinter.printRecord(book, type, chapter, verse.substring(verse.indexOf(">")+1,verse.indexOf("</span>")).trim(),verseText);
					}
				}
				
			}        
			
		}catch(Exception e){
			System.err.println(book);
			e.printStackTrace();
			System.exit(0);
		}
	}
	 
	
}
