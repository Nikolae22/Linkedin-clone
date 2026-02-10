package com.backend.features.authentication.filter;

import com.backend.features.authentication.model.AuthenticationUser;
import com.backend.features.authentication.service.AuthenticationService;
import com.backend.features.authentication.utility.JsonWebToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends HttpFilter {

    private final JsonWebToken jsonWebToken;
    private final AuthenticationService authenticationService;

    private final List<String> unsecuredEndpoints = Arrays.asList(
            "/api/v1/authentication/login",
            "/api/v1/authentication/register",
            "/api/v1/authentication/send-password-reset-tokeb",
            "/api/v1/authentication/reset-password"
    );

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "'GET,POST,PUT,OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        //for cors work when the browser send a request
        // to api from front end e different then frotn url
        //is gonan check server can i get data from u
        // for this to work status ok
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = request.getRequestURI();

        if (unsecuredEndpoints.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new ServletException("Token missing");
            }
            String token = authorization.substring(7);
            if (jsonWebToken.isTokenExpired(token)) {
                throw new ServletException("Invalid token");
            }

            String email=jsonWebToken.getEmailFromToken(token);
            AuthenticationUser user=authenticationService.getUser(email);
            request.setAttribute("authenticationUser",user);
            chain.doFilter(request,response);


        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Invalid authentication token, or token missing.\"}");

        }
    }
}
