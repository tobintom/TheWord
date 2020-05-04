package com.theword.zuuledge.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings(value = { "deprecation" })
@Configuration
@EnableResourceServer
public class ZuulSecurityConfig extends ResourceServerConfigurerAdapter {

	private static final String ROOT_PATTERN = "/theword/v1/**";
	private static final String OAUTH_PATTERN = "/theword/oauth/token";
	
	@Value("${app.security.cert-name}")
    private String publicKey;
	
	private TokenStore tokenStore;
	private JwtAccessTokenConverter converter;
	private String publicCertificate;
	
	@Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
    }
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
		http	.authorizeRequests()
				.antMatchers(HttpMethod.POST,OAUTH_PATTERN).permitAll().and()
        		.authorizeRequests()
                .antMatchers(HttpMethod.GET, ROOT_PATTERN).access("#oauth2.hasScope('trust')")
                .antMatchers(HttpMethod.POST, ROOT_PATTERN).access("#oauth2.hasScope('trust')");
    }
	
	@Bean
    public TokenStore tokenStore() {
        if (tokenStore == null) {
            tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        }
        return tokenStore;
    }
	
	@Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
		if(converter==null) {
			converter = new JwtAccessTokenConverter();
			converter.setVerifierKey(getPublicKeyAsString());
		}
        return converter;
    }

    private String getPublicKeyAsString() {
    	StringBuilder stringBuilder = null;
    	String line = null;
    	try {    	
    		if(publicCertificate == null) {
        	stringBuilder = new StringBuilder();
        	try (BufferedReader bufferedReader = new BufferedReader(
        			new InputStreamReader(new ClassPathResource(publicKey).getInputStream(), UTF_8))) {	
        		while ((line = bufferedReader.readLine()) != null) {
        			stringBuilder.append(line);
        		}
        	}
            publicCertificate = stringBuilder.toString();
    		}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    	return publicCertificate;
    }
}
