package com.szte.SkyScope.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RequestCache requestCache() {
    return new HttpSessionRequestCache();
  }

  @Bean
  public SavedRequestAwareAuthenticationSuccessHandler successHandler() {
    SavedRequestAwareAuthenticationSuccessHandler handler =
        new SavedRequestAwareAuthenticationSuccessHandler();
    handler.setDefaultTargetUrl("/search");
    return handler;
  }

  @Bean
  @Order(1)
  public SecurityFilterChain createOrderSecurity(HttpSecurity http) throws Exception {
    http.securityMatcher("/createOrder/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .formLogin(
            form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/login")
                    .failureForwardUrl("/login?error")
                    .successHandler(successHandler())
                    .permitAll())
        .requestCache(cache -> cache.requestCache(requestCache()))
        .exceptionHandling(
            ex -> ex.authenticationEntryPoint((req, res, e) -> res.sendRedirect("/login")));
    return http.build();
  }

  @Bean
  public SecurityFilterChain defaultSecurity(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth -> auth.requestMatchers("/**").permitAll().anyRequest().denyAll())
        .formLogin(
            form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler(successHandler())
                    .failureUrl("/login?error")
                    .permitAll())
        .logout(logout -> logout.logoutSuccessUrl("/").permitAll());
    return http.build();
  }
}
