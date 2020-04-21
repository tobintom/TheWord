package com.bproject.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.helper.StringUtil;

public class VerifyGeneratedCSV {

	public static void main(String[] args) {
		try{
			
			String bookName = "";
			String chapterName = "";
			int verseCount = 0;
			try (
		            Reader reader =new InputStreamReader(
						    new FileInputStream("C:\\EclipseWorkspace\\TestAsyncClient\\dir\\ENG.csv"), "UTF-8");	
		            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
		        ) {
		            for (CSVRecord csvRecord : csvParser) {
		                // Accessing Values by Column Index
		                String book = csvRecord.get(0);
		                String type = csvRecord.get(1);
		                String chapter = csvRecord.get(2);
		                String verse = csvRecord.get(3);
		                String text = csvRecord.get(4);
		                
		                
		                
		                if(StringUtil.isBlank(book) ||
		                		StringUtil.isBlank(type)||
		                		StringUtil.isBlank(chapter)||
		                		StringUtil.isBlank(verse)||
		                		StringUtil.isBlank(text)||
		                		text.length()<4){
		                	System.err.println("BLANK DATA ON " +book + " - "+chapter + " - " +verse + " - " + text);
		                }
		                
		                if(!bookName.equals(book.trim())){
		                	System.out.println("Chapter "+ chapterName +" has "+ verseCount);
		                	System.out.println("Book "+book );		                	
		                	bookName = book;
		                	chapterName = chapter;
		                }
		                if(!chapterName.equals(chapter.trim())){
		                	System.out.println("Chapter "+ chapterName +" has "+ verseCount);
		                	chapterName = chapter;
		                	verseCount = 0;
		                }
		                
		                verseCount++;
		             //System.out.println("Record No - " + csvRecord.getRecordNumber());
		              
		                 
		            }
		            System.out.println("Chapter "+ chapterName +" has "+ verseCount);
		        }
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
