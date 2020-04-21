package com.bproject.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.JSONObject;

public class GenerateLanguage {
	
	public static void main(String[] args) {
		
		try {
			
			try (Writer writer = new OutputStreamWriter(
				    new FileOutputStream("X:\\EclipseWorkspace\\Datasetup\\dir\\BLANG.json"), "UTF-8");					
			
        ) {		            
		 	JSONObject obj = new JSONObject();
			displayDirectoryContents(new File("X:\\EclipseWorkspace\\Datasetup\\dir\\BLANG.txt"),obj,writer);
			writer.flush();            
        }	
		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private static void displayDirectoryContents(File file, JSONObject obj,Writer writer) {
		try {
			 	String line;		 		    
			    String code = "";
			    String language = "";
			    String dir = "";
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream(file), "UTF-8"))) {
			   
			    while ((line = br.readLine()) != null) {
			    	//System.out.println(line);
			    	String[] contents = line.split("LTR");
			    	if(contents.length>1) {
			    		dir = "LTR";
			    		String[] rest = contents[0].split("-");
			    		code = rest[0].trim();
			    		language = rest[1].trim();
			    	}
			    	else {
			    		contents = line.split("RTL");
			    		dir = "RTL";
			    		String[] rest = contents[0].split("-");
			    		code = rest[0].trim();
			    		language = rest[1].trim();
			    	}
			    	JSONObject compactJson = new JSONObject();  
		    		compactJson.put("code", code);
		    		compactJson.put("language", language);
		    		compactJson.put("dir", dir); 
			    	
			    	
			    	writer.write(compactJson.toString());
		    		writer.write("\n");
		    		code = "";
		    		language = "";
		    		dir = "";
		    		 			    	
			    }
			}catch (Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
