package com.bah.comet.cometapi.config;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bah.comet.cometapi.model.CustomToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Retrieve the auth header, where tokens are being passed through
        String header = request.getHeader(jwtConfig.getHeader());

        // 2. validate header prefix; not valid, move along the chain
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Get the token -- removing the prefix from the token
        String token = header.replace(jwtConfig.getPrefix(), "").trim();

        // exceptions might be thrown in creating the claims -- for example the token is expired
        try {
            // 4. Validate the token
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            int userId =  (int) claims.get("userId");

            if (username != null) {
                List<String> authorities = (List<String>) claims.get("authorities");

                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                CustomToken auth = new CustomToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()), userId );

                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            // In case of failure. Make sure it's clear; guarantee user won't be authenticated
            SecurityContextHolder.clearContext();
        }

        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }
}
