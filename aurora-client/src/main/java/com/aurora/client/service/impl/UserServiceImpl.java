package com.aurora.client.service.impl;

import com.aurora.client.common.dto.UserDTO;
import com.aurora.client.common.entity.UserEntity;
import com.aurora.client.common.vo.UserVO;
import com.aurora.client.exception.ServiceException;
import com.aurora.client.mapper.UserMapper;
import com.aurora.client.security.MyUserDetails;
import com.aurora.client.service.IUserService;
import com.aurora.client.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.aurora.client.common.enumeration.ResultCode.*;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (!authenticate.isAuthenticated()) {   // 认证通过
            throw new ServiceException(USER_PASSWORD_NOT_MATCH);
        }
        // 返回用户的用户编号，用户名，是否付费标识，token，refresh_token
        MyUserDetails myUserDetails = (MyUserDetails) authenticate.getPrincipal();
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(myUserDetails, vo);
        vo.setToken(JwtUtils.generateToken(vo.getUserId()));
        vo.setRefreshToken(JwtUtils.generateRefreshToken(vo.getUserId()));
        return vo;
    }

    /**
     * 刷新token
     */
    @Override
    public UserVO refresh(UserDTO userDTO) {
        String userId = userDTO.getUserId();
        String token = userDTO.getToken();
        String refreshToken = userDTO.getRefreshToken();
        // 判空
        if (StringUtils.isAnyBlank(userId, token, refreshToken)) {
            throw new ServiceException(_403);
        }
        // 验证token合法性-有效期
        if (JwtUtils.ifExpired(token) && JwtUtils.ifExpired(refreshToken)) {
            throw new ServiceException(USER_TOKEN_EXPIRED);
        }
        // 验证token和refresh_token是同一个用户的
        try {
            Claims tokenClaims = JwtUtils.parseToken(token);
            Claims refreshClaims = JwtUtils.parseToken(refreshToken);
            if (!tokenClaims.getSubject().equals(refreshClaims.getSubject())) {
                throw new ServiceException(_403);
            }
        } catch (Exception e) {
            log.error("token验证失败:[{}]", e.getMessage());
            throw new ServiceException(_403);
        }
        // 生成新的token和refresh_token
        token = JwtUtils.generateToken(userId);
        refreshToken = JwtUtils.generateRefreshToken(userId);
        UserVO vo = new UserVO();
        vo.setUserId(userId);
        vo.setToken(token);
        vo.setRefreshToken(refreshToken);

        return vo;
    }
}
