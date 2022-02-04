import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import com.github.scribejava.core.model.Response;
import com.theword.FacebookBatch.FacebookBatch;
import com.theword.TwitterBatch.TDWTwitter;
import com.theword.theWordAPI.TheWordContent;
import com.theword.utils.ImageMaker;

import twitter4j.JSONArray;
import twitter4j.JSONObject;

public class TheWordBatch {
	
	public static void main(String[] args) {
		try {
			trustEveryone();
			
			TheWordBatch wordBatch = new TheWordBatch();
			wordBatch.runAllBatches();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void runAllBatches() {
		TDWTwitter twitter = new TDWTwitter();
		FacebookBatch facebook = new FacebookBatch();
		try {
			ImageMaker im = new ImageMaker();
		
			TheWordContent content = new TheWordContent();
		    Response response = content.getVerseOfTheDay();		    
		    String text = parseResponse(response);
		    byte[] image = im.getContentImage(text);
		    
		    //RunFacebook
		    try {
		    	facebook.runFacebookBatch(image);
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
			
		    //Run Twitter
		    try {
		    	twitter.runTwitterBatch(image);
		    	
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String parseResponse(Response response)throws Exception {

		String retString = "";
		String book;
		String text;
		JSONObject wordString = new JSONObject(response.getBody()); 
		JSONArray arrayJ = (JSONArray)wordString.get("passages");
		JSONObject bookO = (JSONObject)(arrayJ.get(0));
		book = bookO.getString("name");
		book = book +" "+ ((JSONObject)(bookO.getJSONArray("content").get(0))).getString("chapter");
		
		JSONObject verse = (JSONObject)((JSONArray)(((JSONObject)(bookO.getJSONArray("content").get(0))).getJSONArray("verses"))).get(0);
		book = book+":"+verse.getString("verse");
		text = formatString(verse.getString("text"));
		retString = text + System.lineSeparator() +System.lineSeparator() +book;		
		return retString;
	}
	
	private String formatString(String text) {
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
	
	private static  void trustEveryone() { 
	    try { 
	            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){ 
	                    public boolean verify(String hostname, SSLSession session) { 
	                            return true; 
	                    }}); 
	            SSLContext context = SSLContext.getInstance("TLS"); 
	            context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
	                    public void checkClientTrusted(X509Certificate[] chain, 
	                                    String authType) throws CertificateException {} 
	                    public void checkServerTrusted(X509Certificate[] chain, 
	                                    String authType) throws CertificateException {} 
	                    public X509Certificate[] getAcceptedIssuers() { 
	                            return new X509Certificate[0]; 
	                    }}}, new SecureRandom()); 
	            HttpsURLConnection.setDefaultSSLSocketFactory( 
	                            context.getSocketFactory()); 
	    } catch (Exception e) { // should never happen 
	            e.printStackTrace(); 
	    } 
	}

}
