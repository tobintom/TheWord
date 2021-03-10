package com.theword.wordmeta.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	 	@Value("${app.client.id}")
	    private String clientId;
	    @Value("${app.client.secret}")
	    private String clientSecret;
	    @Value("${host.full.dns.auth.link}")
	    private String authLink;
	
	@Bean
    public Docket api() throws Exception {	
		 
	
	        return new Docket(DocumentationType.SWAGGER_2)
	        		.select()
	        		.apis(RequestHandlerSelectors.basePackage("com.theword.wordmeta.rest"))	        		
	                .paths(PathSelectors.any())
	                .build()
	                .securitySchemes(Collections.singletonList(securitySchema()))
	                .securityContexts(Collections.singletonList(securityContext())).pathMapping("/")
	                .useDefaultResponseMessages(false)
	                .apiInfo(apiEndPointsInfo())
	                .useDefaultResponseMessages(false);    
		
//        return new Docket(DocumentationType.SWAGGER_2)  
//          .select() 
//          .apis(RequestHandlerSelectors.basePackage("com.theword.wordcontent.rest"))
//          .paths(PathSelectors.any())                          
//          .build().apiInfo(apiEndPointsInfo())
//        		  .securitySchemes(Arrays.asList(apiKey()));    
        
       
    }
	
	private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("The Word Meta API")
                .description("This API service is free to use and has two micro services, 'The Word Content' and 'The Word Meta'. You can select to view each "
                		+ "from the 'Select a Spec' drop down on the top right corner."
                		+ "\n This set of services is Oauth2 Secured. To try out a service call, you need to be set up. Email us at thewordhelp@gmail.com if you would like a free userId and Password."
                		+ "\n Follow these steps to try out a service call from here:"
                		+ "\n 1. Email us at thewordhelp@gmail.com to get a free ID and Password."
                		+ "\n 2. Click on the 'Authorize' green button on bottom right."
                		+ "\n 3. For username and password, enter the values you get from us (after you are setup)"
                		+ "\n 4. Select 'type' as Request Body. For client_id enter 'theWord-client' and for client_secret enter 'password' "
                		+ "\n 5. Select the 'trust' scope checkbox and click on 'Authorize'. If successful, you will get a confirmation dialog. Close it and you will be able to run test API calls from this page.")
                .version("v1")
                .contact(new Contact("The Word Support", "websiteAddress", "thewordhelp@gmail.com"))
                .license("MIT License")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")              
                .build();
    }
	
	private OAuth securitySchema() {
        List<AuthorizationScope> authorizationScopeList = new ArrayList<AuthorizationScope>();         
        authorizationScopeList.add(new AuthorizationScope("trust", "trust all"));         

        List<GrantType> grantTypes = new ArrayList<GrantType>();
        GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant(authLink+"/oauth/token");
        grantTypes.add(creGrant);
        return new OAuth("oauth2schema", authorizationScopeList, grantTypes);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];         
        authorizationScopes[0] = new AuthorizationScope("trust", "trust all");
        return Collections.singletonList(new SecurityReference("oauth2schema", authorizationScopes));
    }

    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(clientId, clientSecret, "", "", "", ApiKeyVehicle.HEADER, "", " ");
    }

	
	private ApiKey apiKey() {
	    return new ApiKey("jwtToken", "Authorization", "header");
	}


}
