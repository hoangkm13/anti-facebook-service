package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.controller.request.auth.in.setting.PushSettingIn;
import com.example.antifacebookservice.controller.request.auth.in.user.BlockUserIn;
import com.example.antifacebookservice.controller.request.auth.in.version.CheckVersionIn;
import com.example.antifacebookservice.controller.request.auth.out.user.BlockUserOut;
import com.example.antifacebookservice.entity.PushSetting;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.model.ApiResponse;
import com.example.antifacebookservice.service.BlockService;
import com.example.antifacebookservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user")
@CrossOrigin(origins = "http://localhost:8029", allowCredentials = "true")
@RequiredArgsConstructor
public class UserController {
    private final BlockService blockService;
    private final UserService userService;

    @PostMapping("/set-block")
    public ApiResponse<?> setBlock(@RequestBody BlockUserIn blockUserIn) throws CustomException {
        String status = blockService.setBlock(blockUserIn);
        return ApiResponse.successWithResult(null, status);
    }

    @PostMapping("/get-list-blocks")
    public ApiResponse<?> getListBlocks(String token, Integer index, Integer count) throws CustomException {
        List<BlockUserOut> list = blockService.getListBlocked(token, index, count);
        return ApiResponse.successWithResult(list);
    }

    @PostMapping("/get-push-setting")
    public ApiResponse<?> getPushSetting(String token) throws CustomException {
        var settings = userService.getPushSetting(token);
        return ApiResponse.successWithResult(settings);
    }

    @PostMapping("/set-push-setting")
    public ApiResponse<?> setPushSetting(String token, @RequestBody PushSettingIn pushSettingIn) throws IllegalAccessException {
        userService.setPushSetting(token, pushSettingIn);
        return ApiResponse.successWithResult(null, "Setting success!");
    }

    @PostMapping("/check-new-version")
    public ApiResponse<?> checkNewVersion(@RequestBody CheckVersionIn checkVersionIn) throws CustomException {
        return ApiResponse.successWithResult(userService.checkNewVersion(checkVersionIn));
    }
}
