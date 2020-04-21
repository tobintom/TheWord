package com.bproject.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

public class CheckBibleBooks {
	public static void main(String[] args) {
		String[] books =	new String[66];
		for(int i=1;i<67;i++) {
			String book = String.valueOf(i).length()<2?"0"+String.valueOf(i):String.valueOf(i);
			books[i-1] = book;
		}
		
		checkFiles(new File("X:\\EclipseWorkspace\\Datasetup\\dir\\JSON"),books);
	}

	
	private static void checkFiles(File dir, String[] books) {
		try { File[] files = dir.listFiles();
		int i = 0;		
		for(File file:files) {
			Set<String> bookSet = new HashSet<String>();
			
			if(file.getName().length()==8) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream(file), "UTF-8"))) {
				String line = "";
			    while ((line = br.readLine()) != null) {
			    	JSONObject compactJson = new JSONObject(line);
			    	String book = compactJson.get("book").toString();
			    	 
			    	bookSet.add(book);
			    	}	
			    if(bookSet.size()!=66) {
			    	System.out.println("---------------------");
					System.out.println("READING " +file.getName() +" : "+(++i));
					System.out.println("---------------------");
					List<String> missingBooks = new ArrayList<String>();
					for(String a:books) {
						if(!bookSet.contains(a)) {
							missingBooks.add(a);
						}
					}
					
					
			    	System.out.println("Missing Books " +missingBooks);
			    }
			    }        
			        
			  }		
		  }
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
