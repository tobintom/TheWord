package com.theword.FacebookBatch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import com.theword.utils.ImageMaker;
import com.theword.utils.StringUtils;

public class FacebookBatch implements FacebookConfig{
	
	private FacebookClient facebookClient;	 
	 
	public void runFacebookBatch(byte[] image) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get("/home/admin/batch/admin.txt"));
			String oldToken = new String(encoded, StandardCharsets.UTF_8);
			 
			AccessToken accessToken =
					  new DefaultFacebookClient(Version.LATEST).obtainExtendedAccessToken(appID,
					    appSecret, oldToken);
			 			
			 BufferedWriter writer = new BufferedWriter(new FileWriter("/home/admin/batch/admin.txt"));
			 writer.write(accessToken.getAccessToken());
			 writer.close();
			 
			 facebookClient = new DefaultFacebookClient(accessToken.getAccessToken() , Version.LATEST);
			
			 publishContent(image);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	 
	
	private void publishContent(byte[] image)throws Exception {
		String dayMessage = StringUtils.getDayMessage();
		String statusMessage = dayMessage+"\n"+ "Verse of the day #VerseOfTheDay #Bible #TheWordofGod #Inspiration #Blessed";
		ImageMaker im = new ImageMaker();	 

		FacebookType publishPhotoResponse = facebookClient.publish("me/photos", FacebookType.class,
		BinaryAttachment.with("file.jpg", image),
	    Parameter.with("message", statusMessage));
		
		 
		//Instagram
//				FacebookType publishIntagramPhotoResponse = facebookClient.publish(instagram_id+"/media", FacebookType.class,
//						BinaryAttachment.with("file.jpg", image),
//					    Parameter.with("caption", statusMessage));

			    
	}
}

