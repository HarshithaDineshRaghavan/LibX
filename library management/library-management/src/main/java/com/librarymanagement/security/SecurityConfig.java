package com.librarymanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomRequestFilter customRequestFilter;


    private static final String [] publicUrls = {
            "/login", "/css/**", "/js/**", "/images/**"
    };

    private static final String [] adminUrls = {
            "/admin/**"
    };

    private static final String [] userUrls = {
            "/user/**"
    };
      private static final String [] pagesUrls = {
            "/pages/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicUrls).permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("admin")
                        .requestMatchers(userUrls).hasAnyAuthority("user")
                        .requestMatchers(pagesUrls).hasAnyAuthority("pages")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .maximumSessions(2)
                        .expiredUrl("/login?expired")
                )
                .addFilterBefore(customRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}
