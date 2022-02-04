import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;

import com.theword.utils.ImageMaker;

public class TestProject {

	
	public static void main(String[] args)throws Exception {
		TestProject b = new TestProject();
		ImageMaker im = new ImageMaker();
		String te ="യേശു ദൈവപുത്രൻ എന്ന് സ്വീകരിക്കുന്നവനിൽ ദൈവവും, അവൻ ദൈവത്തിലും വസിക്കുന്നു.\n\n 1. യോഹന്നാൻ 4:15 \n" + 
				"Whosoever shall confess that Jesus is the Son of God, God dwelleth in him, and he in God.\n\n 1 john 4:15";
		byte[] image = im.getContentImage(formatString(te));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("a.jpg"));
		bos.write(image);
		bos.flush();
		bos.close();
		
	}
	
	private static String formatString(String text) {
		String ret = "";
		String[] textArray = text.split(" ");
		String interm = "";
		for(String r:textArray) {
			if(interm.length()>45) {
				ret = ret + System.lineSeparator() + interm ;
				interm = "";
			}
			interm = interm +" "+r;
		}
		
		if(interm.trim().length()>0) {
			ret = ret + System.lineSeparator() + interm ;
		}
		
		return ret;
	}
}
