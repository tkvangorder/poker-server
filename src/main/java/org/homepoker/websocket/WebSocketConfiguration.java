package org.homepoker.websocket;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

  public WebSocketConfiguration(ApplicationContext context) {
    this.context = context;
  }

  private final ApplicationContext context;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic", "/queue");
    //registry.setUserDestinationPrefix("/secured");
    registry.setApplicationDestinationPrefixes("/poker");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/connect");
  }

  @Bean
  public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
    messages

        .anyMessage().permitAll();
//        .simpDestMatchers("/user/register").permitAll()
//        .simpSubscribeDestMatchers("/secured/user/queue").permitAll()
//        .simpDestMatchers("/cash-game/admin/**").hasRole("ADMIN")
//        .simpDestMatchers("/**").hasRole("USER")
//        .simpSubscribeDestMatchers("/user**").hasRole("USER")
//        .anyMessage().denyAll();
    return messages.build();
  }


  // Spring Security dies when using @EnableWebSocketSecurity and the issue is related to the CSRF token. It is
  // simply not getting set even though the client is sending it. The following code is essentially doing what the
  // annotation is doing except that it is disabling CSRF protection. I am done fighting with this, it should not
  // take this much effort to get this working.

  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
  }
  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    AuthorizationManager<Message<?>> myAuthorizationRules = AuthenticatedAuthorizationManager.authenticated();
    AuthorizationChannelInterceptor authz = new AuthorizationChannelInterceptor(myAuthorizationRules);
    AuthorizationEventPublisher publisher = new SpringAuthorizationEventPublisher(this.context);
    authz.setAuthorizationEventPublisher(publisher);
    registration.interceptors(new SecurityContextChannelInterceptor(), authz);
  }

  @Bean
  @Scope("prototype")
  MessageMatcherDelegatingAuthorizationManager.Builder messageAuthorizationManagerBuilder(
      ApplicationContext context) {
    return MessageMatcherDelegatingAuthorizationManager.builder().simpDestPathMatcher(
        () -> (context.getBeanNamesForType(SimpAnnotationMethodMessageHandler.class).length > 0)
            ? context.getBean(SimpAnnotationMethodMessageHandler.class).getPathMatcher()
            : new AntPathMatcher());
  }
}
