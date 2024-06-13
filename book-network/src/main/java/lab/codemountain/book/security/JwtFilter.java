package lab.codemountain.book.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // Ensures that the filter is only executed once per request.

    // The service that handles JWT operations like extracting user information
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // The main method where the filter logic is implemented
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // If the request is for authentication, skip the filter and proceed.
        if (request.getServletPath().contains("/api/v1/auth")) { // Checks the URL path of the request.
            filterChain.doFilter(request, response);
            return;
        }

        // Retrieve the Authorization header from the request.
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String userEmail;
        // If the header is missing or does not start with "Bearer ", skip the filter and proceed.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Extract the JWT from the Authorization header.
        jwt = authHeader.substring(7);
        // Use the JwtService to extract the username from the token.
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        logger.debug("Request URL: %s, Auth Header: %s".formatted(request.getRequestURI(), authHeader));
        logger.debug("Security Context Authentication: %s".formatted(SecurityContextHolder.getContext().getAuthentication()));
        filterChain.doFilter(request, response);
    }
}
