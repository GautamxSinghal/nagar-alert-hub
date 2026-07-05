package com.nagaralert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .authorizeHttpRequests(auth -> auth
                // Public paths accessible to everyone (Citizens)
                .requestMatchers("/", "/report", "/alert/**", "/my-alerts", "/login", "/css/**", "/js/**", "/images/**", "/uploads/**", "/error").permitAll()
                // H2 Console
                .requestMatchers(org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                // Admin portal is restricted
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    String dept = request.getParameter("department");
                    if (dept != null && !dept.isEmpty() && !dept.equals("ALL")) {
                        response.sendRedirect("/admin?dept=" + dept);
                    } else {
                        response.sendRedirect("/admin");
                    }
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );

        // We can optionally disable CSRF for H2 console or simple forms if needed,
        // but it's best to leave it enabled and update the thymeleaf templates.
        
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin123") // {noop} means plain text password (for demo purposes)
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
