package org.homepoker.security;

import org.homepoker.user.UserRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties(SecuritySettings.class)
public class RSocketSecurityConfiguration {

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        handler.setRSocketStrategies(strategies);
        return handler;
    }
    
    @Bean
    ReactiveUserDetailsService userDetailsService(UserRepository userRepository, SecuritySettings securitySettings) {
    	return new ReactiveMongoUserDetailsService(userRepository, securitySettings);
    }
    
    @Bean
    PayloadSocketAcceptorInterceptor authorization(RSocketSecurity security) {
        security.authorizePayload(authorize -> authorize
        		.setup().permitAll()
        		.route("user-register").hasRole("ANONYMOUS")
        		.anyExchange().hasAnyRole("USER")
        ).simpleAuthentication(Customizer.withDefaults());
        return security.build();
    }   
    

}
