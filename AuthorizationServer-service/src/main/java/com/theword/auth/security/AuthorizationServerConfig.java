package com.theword.auth.security;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

@SuppressWarnings(value = { "deprecation" })
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Value("${app.client.name}")
    private String clientName;
	
	@Value("${app.client.secret}")
    private String clientSecret;
		
	@Value("${app.keystore.name}")
    private String keystore;
	
	@Value("${app.keystore.pass}")
    private String keystorepass;
	
	@Value("${app.key.name}")
    private String key;
	
	@Value("${app.key.pass}")
    private String keypass;
	
	@Value("${app.ldap.url}")
    private String ldapUrl;
	 	
	@Value("${app.ldap.user-filter}")
    private String ldapUserFilter;
	
    @Value("${app.ldap.admin-user}")
    private String adminUser;

    @Value("${app.ldap.admin-pass}")
    private String adminPassword;

    @Value("${app.ldap.group-base}")
    private String groupBase;    

    @Value("${app.ldap.search-base}")
    private String searchBase;
    
    @Value("${app.oauth.token-validity}")
    private String tokenValidity;
    
    @Value("${app.oauth.refresh-token-validity}")
    private String refreshTokenValidity;
 
    private TokenStore tokenStore;
    JwtAccessTokenConverter converter;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientDetailsService clientDetailsService;
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {    	 
    	endpoints.authenticationManager(authenticationManager)
    	.tokenServices(defaultTokenServices())    	
    	.userDetailsService(ldapUserDetailsManager());
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(clientName)
                .authorizedGrantTypes("client_credentials", "password","refresh_token")
                .authorities("ROLE_CLIENT","ROLE_TRUSTED_CLIENT")
                .scopes("trust")
                .resourceIds("oauth2-resource")
                .accessTokenValiditySeconds(Integer.valueOf(tokenValidity))
                .refreshTokenValiditySeconds(Integer.valueOf(refreshTokenValidity))
                .secret(passwordEncoder.encode(clientSecret));
    }    
    
    @Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer)
			throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

    @Bean
    public DefaultTokenServices defaultTokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());        
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
    
    @Bean
    public TokenStore tokenStore() {
        if (tokenStore == null) {
            tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        }
        return tokenStore;
    }
    
    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
		DefaultSpringSecurityContextSource context = new DefaultSpringSecurityContextSource(ldapUrl);
		context.setUserDn(adminUser);
		context.setPassword(adminPassword);
		return context;
    }
	
	@Bean 
	public FilterBasedLdapUserSearch userSearch() {
		return new FilterBasedLdapUserSearch(searchBase,ldapUserFilter, contextSource());
	}
	
	@Bean
	public LdapUserDetailsService ldapUserDetailsManager() {
		return new LdapUserDetailsService(userSearch());
	}
    
	@Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();        
        List<TokenEnhancer> enhancerList =new ArrayList<TokenEnhancer>();
        enhancerList.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(enhancerList);
        return tokenEnhancerChain;
    }
	
    @Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
    	if(converter==null) {
			converter = new JwtAccessTokenConverter();
			KeyPair keyPair = new KeyStoreKeyFactory(
					new ClassPathResource(keystore), keystorepass.toCharArray())
					.getKeyPair(key, keypass.toCharArray());
			converter.setKeyPair(keyPair);
    	}
		return converter;
	}
    
    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
