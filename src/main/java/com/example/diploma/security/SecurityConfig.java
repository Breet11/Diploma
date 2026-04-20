package com.example.diploma.security;

import com.example.diploma.security.jwt.JwtAuthenticationFilter;
import com.example.diploma.utils.HTTP.HttpSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpSpecs.User.Auth.LOGIN_ENDPOINT,
                                HttpSpecs.User.Auth.REGISTER_ENDPOINT
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, HttpSpecs.Car.GET_CATALOG_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.POST, HttpSpecs.Rental.CREATE_ENDPOINT, HttpSpecs.Rental.CALCULATE_PRICE_ENDPOINT).permitAll()
                        .requestMatchers(HttpSpecs.User.Profile.ALL_ENDPOINTS).authenticated()
                        .requestMatchers(HttpMethod.POST,
                                HttpSpecs.Car.CREATE_ENDPOINT,
                                HttpSpecs.CarBrand.CREATE_ENDPOINT,
                                HttpSpecs.CarModel.CREATE_ENDPOINT,
                                HttpSpecs.CarSpecs.CREATE_ENDPOINT,
                                HttpSpecs.Engine.CREATE_ENDPOINT,
                                HttpSpecs.EngineSpecs.CREATE_ENDPOINT,
                                HttpSpecs.EngineType.CREATE_ENDPOINT,
                                HttpSpecs.LoyaltyRule.CREATE_ENDPOINT
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                HttpSpecs.CarBrand.GET_ALL_ENDPOINT,
                                HttpSpecs.CarModel.GET_ALL_ENDPOINT,
                                HttpSpecs.CarSpecs.GET_ALL_ENDPOINT,
                                HttpSpecs.Engine.GET_ALL_ENDPOINT,
                                HttpSpecs.EngineSpecs.GET_ALL_ENDPOINT,
                                HttpSpecs.EngineType.GET_ALL_ENDPOINT,
                                HttpSpecs.LoyaltyRule.GET_ALL_ENDPOINT,
                                HttpSpecs.Rental.GET_ADMIN_ORDERS_ENDPOINT
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                HttpSpecs.Car.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarBrand.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarModel.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarSpecs.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.Engine.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.EngineType.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.LoyaltyRule.ROOT + HttpSpecs.ANY_SUBPATH
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,
                                HttpSpecs.Car.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarBrand.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarModel.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarSpecs.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.Engine.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.EngineType.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.LoyaltyRule.ROOT + HttpSpecs.ANY_SUBPATH
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                HttpSpecs.Car.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarBrand.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarModel.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.CarSpecs.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.Engine.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.EngineType.ROOT + HttpSpecs.ANY_SUBPATH,
                                HttpSpecs.LoyaltyRule.ROOT + HttpSpecs.ANY_SUBPATH
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpSpecs.Admin.ALL_ENDPOINTS).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
