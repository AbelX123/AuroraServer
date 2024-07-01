package com.aurora.client.service.impl;

import com.aurora.client.entity.User;
import com.aurora.client.mapper.UserMapper;
import com.aurora.client.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
