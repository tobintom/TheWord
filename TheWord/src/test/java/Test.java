import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	public static void main(String[] args) {
		String verse="119:125-126";
		
		String regEx = "\\d{2}\\s+\\d{1,3}:\\d{1,3}(-\\d{1,3})?";
		String regChapterVerse = "\\s+\\d{1,3}:\\d{1,3}(-\\d{1,3})?";
		
		Pattern pattern = Pattern.compile(regChapterVerse);
		
		System.out.println(verse.matches(regEx));
		//String book = verse.split("\\s+")[0];
		//String chapter = verse.split("\\s+")[1].split(":")[0];
		//String verseNumber = verse.split("\\s+")[1].split(":")[1];
		
		Matcher matcher = pattern.matcher(verse);
		if(matcher.find()) {
			System.out.println(verse.replace(matcher.group(),"").trim());
			System.out.println(matcher.group());
		}
		
		//System.out.println(verse.split(";")[1]);
	}

}
