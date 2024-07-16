package com.aurora.client.service;

import com.aurora.client.common.dto.UserDTO;
import com.aurora.client.common.entity.UserEntity;
import com.aurora.client.common.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IUserService extends IService<UserEntity> {

    void signUp(UserDTO userDTO);

    UserVO signIn(UserDTO userDTO);
}
