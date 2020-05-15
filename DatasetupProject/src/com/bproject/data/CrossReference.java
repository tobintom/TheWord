package com.bproject.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class CrossReference {
	
	public static void main(String[] args)throws Exception {

		String[] keys;
		try { 
			CrossReference cr = new CrossReference();
			String[] books =	{"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"};
			
			Map<String, String> bookMap= new HashMap<String, String>();
			
			int m =0;
			for(String book:books) {
				m++;
				bookMap.put(book,(m<10)?"0"+m:String.valueOf(m));				 
			}
			 			
			String line;
			int i = 0;
			Map<String, List<String>> crossMap = new HashMap<String, List<String>>();
			Map<String, List<String>> crossRefMap = new HashMap<String, List<String>>();
			List<String> listroll = null;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				    new FileInputStream("C:\\Users\\tobin\\git\\theword\\DatasetupProject\\DATA\\cross.txt"), "UTF-8"));
					Writer writer = new OutputStreamWriter(
						    new FileOutputStream("C:\\Users\\tobin\\git\\theword\\DatasetupProject\\DATA\\cross_new.txt"), "UTF-8");
					Writer writer1 = new OutputStreamWriter(
						    new FileOutputStream("C:\\Users\\tobin\\git\\theword\\DatasetupProject\\DATA\\CROSS_REF.json"), "UTF-8")) {			   
			    while ((line = br.readLine()) != null) {
			    	++i;
			    	String[] names = line.split("\\s+");
			    	String vote = names[2];
			    	//System.out.println(names[0] +" - " +vote);
			    	if(Integer.parseInt(vote)>3) {
			    			writer.write(line +"\n");
			    			if(crossMap.containsKey(names[0])){
			    				crossMap.get(names[0]).add(names[1]+"|"+vote);
			    			}else {
			    				listroll = new ArrayList<String>();
			    				listroll.add(names[1]+"|"+vote);
			    				crossMap.put(names[0], listroll);
			    				
			    			}
			    			
			    		}
			        }
			    			
			
			crossMap.forEach((k,v)->
			{
				if(((List<String>)v).size()>5) {
					Collections.sort(v, cr.new xcomparator());
					v=v.subList(0, 5);	
					crossRefMap.put(k, v);
				}else {
					crossRefMap.put(k, v); 
				}
			}); 			
			
			Map<String,String> finalMap = new HashMap<String,String>();
			crossRefMap.forEach((k,v)->
			{
				final String[] keys1=((String)k).split("\\.");
				//System.out.println(keys1[0]);
				bookMap.keySet().forEach(bk ->
				{
					
					if(keys1[0].equals("Jas")) {
							finalMap.put(keys1[0], bookMap.get("James"));
						}else
							if(keys1[0].equals("1Kgs")) {
								finalMap.put(keys1[0], bookMap.get("1 Kings"));
							}else if(keys1[0].equals("2Kgs")) {
								finalMap.put(keys1[0], bookMap.get("2 Kings"));
							}else if(keys1[0].equals("Phil")) {
								finalMap.put(keys1[0], bookMap.get("Philippians"));
							}else if(keys1[0].equals("Phlm")) {
								finalMap.put(keys1[0], bookMap.get("Philemon"));
							} else 
								if(bk.replaceAll("\\s", "").startsWith(keys1[0])) {
									finalMap.put(keys1[0] ,  bookMap.get(bk));
							}
				});				
			}
			);			 
			
			finalMap.forEach((k,v)-> System.out.println(k +" : " +v));
			
			SortedMap<String, List<String>> sortedCrossRef = new TreeMap<String, List<String>>();
			
			crossRefMap.forEach((k,v)->
			{
				final String[] keys1=((String)k).split("\\.");
				String key = finalMap.get(keys1[0])+" " + keys1[1]+":"+keys1[2];
				
				if(finalMap.get(keys1[0])==null) {
					System.out.println(k);
				}
				
				List<String> ret = (List<String>)v; 
				List<String> value = new ArrayList<String>();
				ret.forEach(r ->
				{
					String pval = r.split("\\|")[0];
					String[] pvalSplit = pval.split("-");
					if(pvalSplit!=null && pvalSplit.length<2) {
						String[] val=(pvalSplit[0]).split("\\.");
						value.add(finalMap.get(val[0])+" " + val[1]+":"+val[2]);
						if(finalMap.get(val[0])==null) {
							System.out.println((val[0]));
						}
					}else {
						String[] val=(pvalSplit[0]).split("\\.");
						String[] val1=(pvalSplit[1]).split("\\.");
						value.add(finalMap.get(val[0])+" " + val[1]+":"+val[2]+"-"+val1[2]);	
						if(finalMap.get(val[0])==null) {
							System.out.println((val[0]));
						}
					}
					
					sortedCrossRef.put(key, value);
				});				
			});
			
			sortedCrossRef.forEach((k,v)->
			{	
				
				
				JSONObject compactJson = new JSONObject();  
				compactJson.put("book", (k.split(" ")[0]));
				compactJson.put("chapter", (k.split(" ")[1]).split("\\:")[0]);
				compactJson.put("verse", (k.split(" ")[1]).split("\\:")[1]); 
				JSONArray array = new JSONArray();
				array.put(v);
				compactJson.put("references", v);
				
				
		    	try {
		    		writer1.write(compactJson.toString());
		    		writer1.write("\n");
		    	}catch(Exception e) {
		    		e.printStackTrace();
		    	}
			

				
				
				System.out.println(k+":"+v);
			});
			writer.flush();
			writer1.flush();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	public class xcomparator implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
			int v1 = Integer.parseInt(o1.split("\\|")[1]);
			int v2 = Integer.parseInt(o2.split("\\|")[1]);
			return v2-v1;
		}
		
	}
}
