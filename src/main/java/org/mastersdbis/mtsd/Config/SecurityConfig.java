package org.mastersdbis.mtsd.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
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
                    registry.requestMatchers("/home", "/login").permitAll();
 //                   registry.requestMatchers("/admin/home").hasRole("ADMIN");
   //                 registry.requestMatchers("/user/home").hasRole("USER");   Pentru cand facem rolurile, daca le facem
                    registry.anyRequest().authenticated();
                })
                .formLogin(form -> form.defaultSuccessUrl("/home", true).permitAll())
                .build();
    }

}
