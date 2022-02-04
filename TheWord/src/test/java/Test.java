import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	public static void main(String[] args) {
		List<String> numbers = new ArrayList<String>();
		numbers.add("1");
		numbers.add("3");
		numbers.add("4");
		numbers.add("5");
		numbers.add("2");
		numbers.add("8");
		String verses = "";
		List<Integer> inumbers = new ArrayList<Integer>();
		
		for(String a:numbers) {
			inumbers.add(Integer.parseInt(a));
			verses = verses + a +",";
		}
		 if(verses.endsWith(",")){
             verses = verses.substring(0,verses.lastIndexOf(","));
         }
		 System.out.println("VERSES: "+verses);
		
		Collections.sort(inumbers);
		
		StringBuffer g = new StringBuffer();
		int first =0;
		int next = 0;
		int antnext = 0;
		for(Integer d:inumbers) {
			System.out.println(d);
			if(first ==0) {
				first = d;
			}
			if(antnext!=0 && d!=antnext) {
				g.append(first +"-" +antnext);
				first = d;
				antnext = d+1;
			}else {
				antnext = d+1;
			}
		}
		
		
		
		
		System.out.println(ConvertToRanges("1,3,4,5,2,8"));
		
	}
	
	 public static String ConvertToRanges(String pageNos)
	    {
	    	String result="";
	    	String[] arr1 = pageNos.split(",");
	    	int[] arr = new int[arr1.length];
	 
	    	for (int x = 0; x < arr1.length; x++) // Convert string array to integer array
	    	{
	    		arr[x] = Integer.parseInt(arr1[x]);
	    	}
	 
	    	int start,end;  // track start and end
	    	end = start = arr[0];
	    	for (int i = 1; i < arr.length; i++)
	    	{
	    		// as long as entries are consecutive, move end forward
	    		if (arr[i] == (arr[i - 1] + 1))
	    		{
	    			end = arr[i];
	    		}
	    		else
	    		{
	    			// when no longer consecutive, add group to result
	    			// depending on whether start=end (single item) or not
	    			if (start == end)
	    				result += start + ",";
	    			else
	    				result += start + "-" + end + ",";
	 
	    			start = end = arr[i];
	    		}
	    	}
	 
	    	// handle the final group
	    	if (start == end)
	    		result += start;
	    	else
	    		result += start + "-" + end;
	 
	    	return result;
	    }

}
