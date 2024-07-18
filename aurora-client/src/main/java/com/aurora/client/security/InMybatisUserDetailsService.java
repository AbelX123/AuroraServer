package com.aurora.client.security;

import com.aurora.client.common.entity.UserEntity;
import com.aurora.client.exception.ServiceException;
import com.aurora.client.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

import static com.aurora.client.common.enumeration.ResultCode.USER_PASSWORD_NOT_MATCH;

/**
 * 以mybatis plus加载用户数据
 */
@Service
public class InMybatisUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 封装查询条件
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getUserName, username);
        UserEntity userEntity = userMapper.selectOne(wrapper);
        if (Objects.isNull(userEntity)) {
            throw new ServiceException(USER_PASSWORD_NOT_MATCH);
        }
        return new MyUserDetails(userEntity.getUserId(), username, userEntity.getUserPassword(), Collections.emptySet());
    }
}
