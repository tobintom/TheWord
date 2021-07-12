package com.theword.TwitterBatch;

import java.io.ByteArrayInputStream; 
import com.theword.utils.ImageMaker;
import com.theword.utils.StringUtils;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;

public class TDWTwitter {
	 
	
	TwitterFactory tf = null;
	Twitter twitter = null;
	
	public void runTwitterBatch(byte[] image) {
		
		try {			  
			 tf = new TwitterFactory(TwitterConfigurationBuilder.getConfigBuilder().build());
			 twitter = tf.getInstance();
			 publishContent(image);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void publishContent(byte[] image) {
			 
	try {		 
			String dayMessage = StringUtils.getDayMessage();
			String statusMessage = dayMessage+"\n"+ "Verse of the day #VerseOfTheDay #Bible #TheWordofGod #Inspiration #Blessed";
			 			  
			 ImageMaker im = new ImageMaker(); 			    
			 long[] mediaIds = new long[1];
			 UploadedMedia media = twitter.uploadMedia("name", new ByteArrayInputStream(image));
			 mediaIds[0] = media.getMediaId();

				StatusUpdate statusUpdate = new StatusUpdate(statusMessage);
				statusUpdate.setMediaIds(mediaIds);
				Status status = twitter.updateStatus(statusUpdate);
			
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	 
	
	
}
