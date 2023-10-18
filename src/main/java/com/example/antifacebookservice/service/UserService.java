package com.example.antifacebookservice.service;

import com.example.antifacebookservice.controller.request.auth.ResetPasswordDTO;
import com.example.antifacebookservice.controller.request.auth.SignUpDTO;
import com.example.antifacebookservice.controller.request.auth.UpdateUserDTO;
import com.example.antifacebookservice.controller.request.auth.UserDTO;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User findByUsername(String username) throws CustomException, IOException;
    User createUser(SignUpDTO signUpDTO) throws CustomException;
    void checkPermission(String userId) throws CustomException;
    User findById(String UserId) throws CustomException;
    User preCheckUpdateUserInfo(UpdateUserDTO updateUserDTO, String currentUserId, String userId, MultipartFile avatarFile) throws CustomException, IOException;
    User resetPassword(ResetPasswordDTO resetPasswordDTO, String currentUserId, String userId) throws CustomException;
}
