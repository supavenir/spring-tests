package fr.caensup.td4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // (1)
@EnableWebSecurity // (1)
public class WebSecurityConfig {

  @Bean // (2)
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/hello/**", "/index", "/", "/rest/**").permitAll() // (3)
        .anyRequest().authenticated() // (4)
        .and().formLogin() // (5)
        .permitAll().and().logout() // (6)
        .permitAll().and().httpBasic(); // (7)
    http.headers().frameOptions().sameOrigin(); // (8)
    return http.build();
  }

}
