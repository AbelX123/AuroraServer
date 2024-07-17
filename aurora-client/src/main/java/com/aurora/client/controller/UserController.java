package com.aurora.client.controller;

import com.aurora.client.common.CommonResult;
import com.aurora.client.common.dto.UserDTO;
import com.aurora.client.common.vo.UserVO;
import com.aurora.client.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/signUp")
    public CommonResult<?> signUp(@RequestBody @Validated UserDTO userDTO) {
        userService.signUp(userDTO);
        return CommonResult.success();
    }

    @PostMapping("/signIn")
    public CommonResult<UserVO> login(@RequestBody @Validated UserDTO userDTO) {
        return CommonResult.success(userService.signIn(userDTO));
    }

    @GetMapping("/hello")
    public CommonResult<?> hello() {
        return CommonResult.success("hello");
    }
}
