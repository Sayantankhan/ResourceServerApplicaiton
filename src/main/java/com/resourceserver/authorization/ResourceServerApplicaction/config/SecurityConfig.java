package com.resourceserver.authorization.ResourceServerApplicaction.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.IssuerClaimVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.resourceserver.authorization.ResourceServerApplicaction.config.entrypoint.AuthExceptionEntryPoint;

@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer config) {
		config.tokenServices(createTokenServices())
			.authenticationEntryPoint(new AuthExceptionEntryPoint());
	}
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
	
		http
			.anonymous().disable()
        	.csrf().disable()
        	.antMatcher("/hello").authorizeRequests()
        	.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                    "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-ui.html",
                    "/swagger-resources/configuration/security").permitAll()
            .anyRequest().authenticated();
    }
	
	@Bean
	public DefaultTokenServices createTokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(createTokenStore());
		return defaultTokenServices;
	}

	@Bean
	public TokenStore createTokenStore() {
		return new JwtTokenStore(createJwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter createJwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("Sayantan");
		//converter.setJwtClaimsSetVerifier(issuerClaimVerifier());
		converter.setAccessTokenConverter(new JWTConveter());
		return converter;
	}
	
	
	@Bean
	public JwtClaimsSetVerifier issuerClaimVerifier() {
	    try {
	        return new IssuerClaimVerifier(new URL("http://localhost:8081"));
	    } catch (MalformedURLException e) {
	        throw new RuntimeException(e);
	    }
	}
}
