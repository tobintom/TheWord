package com.theword.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${app.ldap.url}")
    private String ldapUrl;
	 	
	@Value("${app.ldap.user-dn}")
    private String ldapUserDnPatterns;
	
    @Value("${app.ldap.admin-user}")
    private String adminUser;

    @Value("${app.ldap.admin-pass}")
    private String adminPassword;


    @Value("${app.ldap.group-base}")
    private String groupBase;
	
    private static final String OAUTH_PATTERN = "/oauth/token";
    
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.ldapAuthentication()
			.contextSource()
			.url(ldapUrl)
			.managerDn(adminUser)
			.managerPassword(adminPassword)
			.and()
			.userDnPatterns(ldapUserDnPatterns)
			.groupSearchBase(groupBase);
			
			 
    }
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
        http
        	.cors().and()
            .authorizeRequests()
                .antMatchers(OAUTH_PATTERN).permitAll().and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
	
	 @Bean
     CorsConfigurationSource corsConfigurationSource() {
         final UrlBasedCorsConfigurationSource source = new     UrlBasedCorsConfigurationSource();
         final CorsConfiguration config = new CorsConfiguration();
         config.setAllowCredentials(true);
         config.addAllowedOrigin(CorsConfiguration.ALL);
         config.addAllowedHeader(CorsConfiguration.ALL);
         config.addAllowedMethod("OPTIONS");
         config.addAllowedMethod("HEAD");
         config.addAllowedMethod("GET");
         config.addAllowedMethod("PUT");
         config.addAllowedMethod("POST");
         config.addAllowedMethod("DELETE");
         config.addAllowedMethod("PATCH");
         source.registerCorsConfiguration("/**", config);
         return source;
     }
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
 	