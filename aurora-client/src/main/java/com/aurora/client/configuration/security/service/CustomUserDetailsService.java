package com.aurora.client.configuration.security.service;

import com.aurora.client.common.entity.UserEntity;
import com.aurora.client.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        Optional<UserEntity> one = Optional.ofNullable(userService.getOne(queryWrapper));

        return one.map(user -> User.withUsername(user.getUserName())
                .password(user.getUserPassword())
                .build()).orElseThrow(() -> new UsernameNotFoundException("用户 " + username + " 未找到"));
    }
}
