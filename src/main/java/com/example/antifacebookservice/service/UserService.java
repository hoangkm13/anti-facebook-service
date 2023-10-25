package com.example.antifacebookservice.service;

import com.example.antifacebookservice.controller.request.auth.CheckCodeVerifyRequest;
import com.example.antifacebookservice.controller.request.auth.ResetPasswordDTO;
import com.example.antifacebookservice.controller.request.auth.SignUpDTO;
import com.example.antifacebookservice.controller.response.CheckVerifyCodeResponse;
import com.example.antifacebookservice.controller.response.GetCodeVerifyResponse;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User findByUsername(String username) throws CustomException, IOException;

    void createUser(SignUpDTO signUpDTO) throws CustomException, IOException, InterruptedException;

    GetCodeVerifyResponse getCodeVerify(String email) throws CustomException, IOException, InterruptedException;

    CheckVerifyCodeResponse checkVerifyCode(CheckCodeVerifyRequest checkCodeVerifyRequest) throws CustomException, IOException, InterruptedException;

    void checkPermission(String userId) throws CustomException;

    User findById(String UserId) throws CustomException;

    User changeInfoAfterSignUp(String username, String currentUserId, String userId, MultipartFile avatarFile) throws CustomException, IOException;

    User resetPassword(ResetPasswordDTO resetPasswordDTO, String currentUserId, String userId) throws CustomException;
}
