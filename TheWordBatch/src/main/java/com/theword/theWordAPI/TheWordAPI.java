package com.theword.theWordAPI;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class TheWordAPI extends DefaultApi20{
	
	public TheWordAPI() {
		
	}
	
	private static class InstanceHolder {
        private static final TheWordAPI INSTANCE = new TheWordAPI();
    }

    public static TheWordAPI instance() {
        return InstanceHolder.INSTANCE;
    }

	@Override
	public String getAccessTokenEndpoint() {		 
		return "https://thedigitalword.org:5000/theword/oauth/token";
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
