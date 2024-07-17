package com.aurora.client.filter;

import com.aurora.client.common.enumeration.HttpHeaders;
import com.aurora.client.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从header中获取token
        String token = request.getHeader(HttpHeaders.AUTHORIZATION_TOKEN);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token获取userId
        String userId;
        try {
            Claims claims = JwtUtils.parseToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            log.error("token解析失败:[{}]", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }
        // 检查token是否已经过期
        boolean expired = JwtUtils.ifExpired(token);
        if (expired) {
            filterChain.doFilter(request, response);
            return;
        }

        // 封装Authentication对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptySet());
        // 将Authentication存入spring security上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 链式调用
        filterChain.doFilter(request, response);
    }
}
