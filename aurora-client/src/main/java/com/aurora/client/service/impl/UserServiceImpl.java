package com.aurora.client.service.impl;

import com.aurora.client.common.dto.UserDTO;
import com.aurora.client.common.entity.UserEntity;
import com.aurora.client.common.vo.UserVO;
import com.aurora.client.exception.ServiceException;
import com.aurora.client.mapper.UserMapper;
import com.aurora.client.redis.RedisPrefix;
import com.aurora.client.redis.RedisService;
import com.aurora.client.security.MyUserDetails;
import com.aurora.client.service.IUserService;
import com.aurora.client.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.aurora.client.common.enumeration.ResultCode.USER_EXIST;
import static com.aurora.client.common.enumeration.ResultCode.USER_PASSWORD_NOT_MATCH;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisService redisService;

    /**
     * 注册用户
     */
    @Override
    public void signUp(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        // 检查数据库中是否已经有该用户
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getUserName, username);
        Optional<UserEntity> oneOpt = this.getOneOpt(wrapper);
        if (oneOpt.isPresent()) {
            throw new ServiceException(USER_EXIST);
        }
        // 使用BCrypt加密密码
        String encodedPass = passwordEncoder.encode(password);
        // 存储数据
        UserEntity entity = new UserEntity();
        entity.setUserId(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
        entity.setUserName(username);
        entity.setUserPassword(encodedPass);
        entity.setUserLevel("0");
        this.save(entity);
    }

    /**
     * 用户登录
     */
    @Override
    public UserVO signIn(UserDTO userDTO) {
        String username = userDTO.getUsername();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, userDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (!authenticate.isAuthenticated()) {   // 认证通过
            throw new ServiceException(USER_PASSWORD_NOT_MATCH);
        }
        // 返回用户的用户编号，用户名，是否付费标识，token，refresh_token
        MyUserDetails myUserDetails = (MyUserDetails) authenticate.getPrincipal();
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(myUserDetails, vo);
        String token = JwtUtils.generateToken(vo.getUserId());
        vo.setToken(token);
        String refreshToken = JwtUtils.generateRefreshToken(vo.getUserId());
        vo.setRefreshToken(refreshToken);

        String webTokenKey = RedisPrefix.PREFIX_WEB_TOKEN + myUserDetails.getUserId();
        String webRefreshTokenKey = RedisPrefix.PREFIX_WEB_REFRESH_TOKEN + myUserDetails.getUserId();

        // 存储用户新的token，这里直接使用set方法，没有就添加，有就覆盖
        redisService.set(webTokenKey, token);
        redisService.set(webRefreshTokenKey, refreshToken);

        return vo;
    }

    /**
     * 刷新token
     */
    @Override
    public UserVO refresh() {
        // 链路认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getPrincipal().toString();
        String token = JwtUtils.generateToken(userId);
        String refreshToken = JwtUtils.generateRefreshToken(userId);
        UserVO vo = new UserVO();
        vo.setUserId(userId);
        vo.setToken(token);
        vo.setRefreshToken(refreshToken);

        // 刷新缓存
        String webTokenKey = RedisPrefix.PREFIX_WEB_TOKEN + userId;
        String webRefreshTokenKey = RedisPrefix.PREFIX_WEB_REFRESH_TOKEN + userId;
        redisService.set(webTokenKey, token);
        redisService.set(webRefreshTokenKey, refreshToken);

        return vo;
    }
}
