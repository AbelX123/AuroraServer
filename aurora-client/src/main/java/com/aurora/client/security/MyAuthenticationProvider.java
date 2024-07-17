package com.aurora.client.security;

import com.aurora.client.exception.ServiceException;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.aurora.client.common.enumeration.ResultCode.USER_PASSWORD_NOT_MATCH;

/**
 * 认证信息验证
 */
@Data
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 从数据库中取出密码进行比较
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        String password = authentication.getCredentials().toString();
        boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
        if (!matches) {
            throw new ServiceException(USER_PASSWORD_NOT_MATCH);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
