package com.theword.zuuledge.filters;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

public class RouteFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() { 
		return false;
	}

	@Override
	public Object run() throws ZuulException { 
		return null;
	}

	@Override
	public String filterType() { 
		return FilterConstants.ROUTE_TYPE;
	}

	@Override
	public int filterOrder() { 
		return 0;
	}

}
