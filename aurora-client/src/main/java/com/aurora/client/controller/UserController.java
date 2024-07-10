package com.aurora.client.controller;

import com.aurora.client.common.CommonResult;
import com.aurora.client.common.dto.UserDTO;
import com.aurora.client.common.vo.UserVO;
import com.aurora.client.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public CommonResult<UserVO> login(@RequestBody @Validated UserDTO userDTO) {
        return null;
    }
}
