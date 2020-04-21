package com.bproject.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestString {

	
	public static void main(String[] args) throws Exception{
//		String line = "\\v 2-21 TEXT VERSE\\x - \\xo 1:2 \\xq comment\\xt comment 4:23-25, comment 14:12-15, comment 28:11-19, comment 9:7.\\x*Text Verse CONT; Text Verse."; 
//		String verse = "";
//	    String verseText;
//		System.out.println(line);
//		if(line.startsWith("\\v")){
//			Pattern p = Pattern.compile("\\d+-\\d+");
//			 Matcher m = p.matcher(line);
//			 if(m.find()) {
//				 verse = m.group();		             
//		        }			 
//            verseText = extractVerseText(line,verse);
//            System.out.println(verse);
            //System.out.println(verseText);
        	 
        	 
       // }
		
		System.out.println(Integer.parseInt("001"));
		 
	}
	
	
	private static String extractVerseText(String rawVerse,String verse){
		String returnVerse = "";
		int length = (verse+ " ").length();
		returnVerse = (rawVerse.substring(rawVerse.indexOf(verse)+length));		
		String textToRemove = returnVerse.substring(returnVerse.indexOf("\\"), returnVerse.lastIndexOf("*")+1);		
		returnVerse = (returnVerse.replace(textToRemove, ""));
		return returnVerse;
	}
}
