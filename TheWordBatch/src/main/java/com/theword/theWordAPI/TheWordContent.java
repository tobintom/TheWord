package com.theword.theWordAPI;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

public class TheWordContent {
	
	public Response getVerseOfTheDay() {
		Response response = null;
		try {
		OAuth20Service service = ServiceGenerator.generateService();
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://thedigitalword.org:5000/theword/v1/bible/eng/randomDailyVerse");
		service.signRequest(ServiceGenerator.getToken(), request);
		response = service.execute(request);
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
