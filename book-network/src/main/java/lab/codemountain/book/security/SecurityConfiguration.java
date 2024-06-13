package lab.codemountain.book.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http /* Configure SecurityFilterChain: A series of tests that every request to the app must pass through.
                Below methods are used to configure various aspects (cors, csrf, auth) of the filter chain.*/
                .cors(withDefaults()) // Allows requests from other origins to access app resources; Here allows all origins without restrictions.
                .csrf(AbstractHttpConfigurer::disable) // Disables CSRF (Cross-Site Request Forgery) protection.
                .authorizeHttpRequests(request -> // Define accessible endpoints.
                        request.requestMatchers( // Permitted endpoints.
                                "/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html"
                        ).permitAll() // Any other request is required to be authenticated.
                                .anyRequest().authenticated()
                )
                // Configure session management
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // Configure to not create and store sessions for users.
                .authenticationProvider(authenticationProvider) // Configure authentication provider with custom implementation to handle authentication of user credentials.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add filter before UsernamePasswordAuthenticationFilter to handle JWT tokens.

        return http.build();
    }
}
