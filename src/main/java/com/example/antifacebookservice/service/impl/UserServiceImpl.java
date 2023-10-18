package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.ErrorCode;
import com.example.antifacebookservice.constant.Role;
import com.example.antifacebookservice.controller.request.auth.ResetPasswordDTO;
import com.example.antifacebookservice.controller.request.auth.SignUpDTO;
import com.example.antifacebookservice.controller.request.auth.UpdateUserDTO;
import com.example.antifacebookservice.controller.request.auth.UserDTO;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.repository.UserRepository;
import com.example.antifacebookservice.service.UserService;
import com.example.antifacebookservice.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtils authUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthUtils authUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUtils = authUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        User User = optionalUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
//        GrantedAuthority authority = new SimpleGrantedAuthority(User.getRole());
//        authorities.add(authority);
        return new org.springframework.security.core.userdetails.User(User.getUsername(), User.getPasswordHash(), authorities);
    }

    @Override
    public User findByUsername(String username) throws CustomException, IOException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_EXIST);
        }
//        File file = new File(FileConfig.PRE_PATH_AVATAR + optionalUser.get().getId() + ".jpg");
//
//        if (file.exists()) {
//            optionalUser.get().setAvatar(Arrays.toString(FileUtils.convertFileToBytes(file)));
//        }
        return optionalUser.get();
    }

    @Override
    public User createUser(SignUpDTO signUpDTO) throws CustomException {
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_EXIST);
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
//        User.setGender(UserDTO.getGender());
//        User.setBirthOfDate(UserDTO.getBirthOfDate());
//        User.setMobile(UserDTO.getMobile());
//        User.setRole(Role.USER.getValue());
        user.setUsername(signUpDTO.getEmail());
        user.setEmail(signUpDTO.getEmail());
//        User.setFirstName(signUpDTO.getFirstName());
//        User.setLastName(signUpDTO.getLastName());
//        User.setCountry(signUpDTO.getCountry());
        user.setPasswordHash(passwordEncoder.encode(signUpDTO.getPassword()));

//        notificationRepository.save(new Notification(null,
//                null,
//                null,
//                User.getId(),
//                "feb0978d-b7b6-4bb9-9a85-96f9cc0ed5de",
//                ""));

        return userRepository.save(user);
    }

    @Override
    public void checkPermission(String userId) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = findById(authentication.getName());
        if (!userId.equals(currentUser.getId()) && !authUtils.isAdmin()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public User findById(String UserId) throws CustomException {
        var user = this.userRepository.findById(UserId);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_EXIST);
        }

        return user.get();
    }

    @Override
    public User preCheckUpdateUserInfo(UpdateUserDTO updateUserDTO, String currentUserId, String UserId, MultipartFile avatarFile) throws CustomException, IOException {
        var existedUser = this.findById(currentUserId);

        if (!Objects.equals(UserId, existedUser.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return this.updateUser(updateUserDTO, existedUser, avatarFile);
    }

    @Override
    public User resetPassword(ResetPasswordDTO resetPasswordDTO, String currentUserId, String userId) throws CustomException {
        var existedUser = this.findById(currentUserId);

        if (!Objects.equals(userId, existedUser.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        existedUser.setPasswordHash(passwordEncoder.encode(resetPasswordDTO.getPassword()));

        return this.userRepository.save(existedUser);
    }

    private User updateUser(UpdateUserDTO updateUserDTO, User existedUser, MultipartFile avatarFile) throws IOException {

//        FileUtils fileUtils = new FileUtils();

//        existedUser.setGender(updateUserDTO.getGender() != null ? updateUserDTO.getGender() : existedUser.getGender());
//        existedUser.setBirthOfDate(updateUserDTO.getBirthOfDate() != null ? updateUserDTO.getBirthOfDate() : existedUser.getBirthOfDate());
//        existedUser.setMobile(updateUserDTO.getMobile() != null ? updateUserDTO.getMobile() : existedUser.getMobile());
        existedUser.setEmail(updateUserDTO.getEmail() != null ? updateUserDTO.getEmail() : existedUser.getEmail());
//        existedUser.setFirstName(updateUserDTO.getFirstName() != null ? updateUserDTO.getFirstName() : existedUser.getFirstName());
//        existedUser.setLastName(updateUserDTO.getLastName() != null ? updateUserDTO.getLastName() : existedUser.getLastName());
        existedUser.setEmail(updateUserDTO.getEmail() != null ? updateUserDTO.getEmail() : existedUser.getEmail());
//        existedUser.setGithub(updateUserDTO.getGithub() != null ? updateUserDTO.getGithub() : existedUser.getGithub());
//        existedUser.setFacebook(updateUserDTO.getFacebook() != null ? updateUserDTO.getFacebook() : existedUser.getFacebook());
//        existedUser.setWebsite(updateUserDTO.getWebsite() != null ? updateUserDTO.getWebsite() : existedUser.getWebsite());
        existedUser.setAvatar(existedUser.getId());
//        existedUser.setCountry(updateUserDTO.getCountry() != null ? updateUserDTO.getCountry() : existedUser.getCountry());

//        if(avatarFile != null && !avatarFile.isEmpty()){
//            fileUtils.createFileSave(avatarFile, FileConfig.PRE_PATH_AVATAR, existedUser.getId() + ".jpg");
//        }

        this.userRepository.save(existedUser);

        return existedUser;
    }
}
