package com.example.seugoi_back.Jwt;

import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);

            // 토큰 만료 시 401 반환
            if (!jwtTokenProvider.validateToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }

            Long userCode = jwtTokenProvider.getUserCode(token);

            User user = userRepository.findById(userCode).orElse(null);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
