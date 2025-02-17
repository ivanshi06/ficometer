package com.config;

import org.springframework.context.annotation.Bean;
 
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.List;
@Configuration
@EnableWebMvc
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthentication jwtAuthentication;
    public static final String[] PUBLIC_URLS= {
    		"/v3/api-docs",
    		"/v2/api-docs",
    		"/swagger-resources/**",
    		"/swagger-ui/**",
    		"/webjars/**",
    };
    public SecurityConfiguration(
        JwtAuthentication jwtAuthentication,
        AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthentication = jwtAuthentication;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers("/product/add/**").permitAll()
                .requestMatchers("/review/add/**").permitAll()
                .requestMatchers("/product/vendorId/**").permitAll()
                .requestMatchers("/vendor/**").hasAuthority("VENDOR")
                .requestMatchers("/user/**").hasAnyAuthority("USER","VENDOR","ADMIN")
//              	.requestMatchers("/products/**").hasAuthority("USER")
                .requestMatchers("/product/**").permitAll()
                .requestMatchers("/product/findBySubCategory/**").permitAll()
                .requestMatchers("/category/**").permitAll()
                .requestMatchers("/subcategory/**").permitAll()
                .requestMatchers("/admin/dashboard/**").permitAll()
                .requestMatchers("/cartitem/**").permitAll()
                .requestMatchers("/faq/**").permitAll()
                .requestMatchers("/order/placeorder/**").permitAll()
                .requestMatchers("/user/verify-email/**").permitAll()
                .requestMatchers("/auth/**")
                .permitAll()
//                .anyRequest()
//                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthentication, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8085"));
        configuration.setAllowedMethods(List.of("GET","POST"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
