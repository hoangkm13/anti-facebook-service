package com.example.antifacebookservice.service;

import com.example.antifacebookservice.constant.SettingStatus;
import com.example.antifacebookservice.controller.request.auth.CheckCodeVerifyRequest;
import com.example.antifacebookservice.controller.request.auth.ResetPasswordDTO;
import com.example.antifacebookservice.controller.request.auth.SignUpDTO;
import com.example.antifacebookservice.controller.request.auth.in.setting.PushSettingIn;
import com.example.antifacebookservice.controller.request.auth.in.version.CheckVersionIn;
import com.example.antifacebookservice.controller.request.auth.out.version.CheckVersionOut;
import com.example.antifacebookservice.controller.response.CheckVerifyCodeResponse;
import com.example.antifacebookservice.controller.response.GetCodeVerifyResponse;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UserService {
    User findByUsername(String username) throws CustomException;

    void createUser(SignUpDTO signUpDTO) throws CustomException, IOException, InterruptedException;

    GetCodeVerifyResponse getCodeVerify(String email) throws CustomException, IOException, InterruptedException;

    CheckVerifyCodeResponse checkVerifyCode(CheckCodeVerifyRequest checkCodeVerifyRequest) throws CustomException, IOException, InterruptedException;

    void checkPermission(String userId) throws CustomException;

    User findById(String UserId) throws CustomException;

    User changeInfoAfterSignUp(String username, String currentUserId, String userId, MultipartFile avatarFile) throws CustomException, IOException;

    User resetPassword(ResetPasswordDTO resetPasswordDTO, String currentUserId, String userId) throws CustomException;

    Map<String, SettingStatus> getPushSetting(String token) throws CustomException;

    void setPushSetting(String token, PushSettingIn pushSettingIn) throws IllegalAccessException;

    CheckVersionOut checkNewVersion(CheckVersionIn checkVersionIn) throws CustomException;
}
