package org.mastersdbis.mtsd.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/auth/register", "/auth/login", "/css/**", "/js/**").permitAll()
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/provider/**").hasRole("PROVIDER")
                            .requestMatchers("/users/addProvider").hasRole("CLIENT")
                            .requestMatchers("/users/providers/{providerId}/validate").hasAnyRole("ADMIN")
                            .requestMatchers("/users/providers/{providerId}/deny").hasAnyRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().write("Autentificare reusita");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write("Autentificare esuata");
                        })
                        .permitAll())
                .logout(logout -> logout.logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().write("Deconectare reusita");
                }))
                .exceptionHandling(exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Acces interzis");
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
