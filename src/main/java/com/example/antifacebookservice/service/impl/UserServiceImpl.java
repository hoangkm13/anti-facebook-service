package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.auth.CheckCodeVerifyRequest;
import com.example.antifacebookservice.controller.request.auth.ResetPasswordDTO;
import com.example.antifacebookservice.controller.request.auth.SignUpDTO;
import com.example.antifacebookservice.controller.request.auth.UpdateUserDTO;
import com.example.antifacebookservice.controller.response.CheckVerifyCodeResponse;
import com.example.antifacebookservice.controller.response.GetCodeVerifyResponse;
import com.example.antifacebookservice.entity.CodeVerify;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.repository.CodeVerifyRepository;
import com.example.antifacebookservice.repository.UserRepository;
import com.example.antifacebookservice.service.UserService;
import com.example.antifacebookservice.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final CodeVerifyRepository codeVerifyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtils authUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CodeVerifyRepository codeVerifyRepository, PasswordEncoder passwordEncoder, AuthUtils authUtils) {
        this.userRepository = userRepository;
        this.codeVerifyRepository = codeVerifyRepository;
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
    public User findByUsername(String username) throws CustomException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ResponseCode.USER_IS_NOT_VALIDATED);
        }

        return optionalUser.get();
    }

    @Override
    public void createUser(SignUpDTO signUpDTO) throws CustomException {
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new CustomException(ResponseCode.USER_EXISTED);
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(signUpDTO.getEmail());
        user.setEmail(signUpDTO.getEmail());
        user.setActive(false);
        user.setPasswordHash(passwordEncoder.encode(signUpDTO.getPassword()));
        userRepository.save(user);

        getCodeVerify(signUpDTO.getEmail());
    }

    @Override
    public GetCodeVerifyResponse getCodeVerify(String email) throws CustomException {
        var codeVerifyExisted = codeVerifyRepository.findCodeVerifiesByUsername(email);
        codeVerifyExisted.ifPresent(codeVerifyRepository::delete);
        findByUsername(email);

        int randomNumber = generateRandomNumber();

        LocalTime currentTime = LocalTime.now();

        CodeVerify codeVerify = new CodeVerify();
        codeVerify.setCodeVerify(randomNumber);
        codeVerify.setUsername(email);
        codeVerify.setCreatedTime(currentTime);

        codeVerifyRepository.save(codeVerify);

        GetCodeVerifyResponse getCodeVerifyResponse = new GetCodeVerifyResponse();
        getCodeVerifyResponse.setCodeVerify(randomNumber);

        return getCodeVerifyResponse;

    }

    @Override
    public CheckVerifyCodeResponse checkVerifyCode(CheckCodeVerifyRequest checkCodeVerifyRequest) throws CustomException, IOException, InterruptedException {
        var code = this.codeVerifyRepository.findCodeVerifiesByUsername(checkCodeVerifyRequest.getEmail());
        var user = findByUsername(checkCodeVerifyRequest.getEmail());

        if (code.isEmpty()) {
            throw new CustomException(ResponseCode.CODE_VERIFY_IS_INCORRECT);
        }

        if (!code.get().getCodeVerify().equals(checkCodeVerifyRequest.getCodeVerify())) {
            throw new CustomException(ResponseCode.PARAMETER_VALUE_IS_INVALID);
        }

        LocalTime currentTime = LocalTime.now();

        if (code.get().getCreatedTime().plusSeconds(60).isAfter(currentTime)) {

            user.setActive(true);
            this.userRepository.save(user);
            this.codeVerifyRepository.delete(code.get());

            CheckVerifyCodeResponse checkVerifyCodeResponse = new CheckVerifyCodeResponse();
            checkVerifyCodeResponse.setActive(true);
            checkVerifyCodeResponse.setId(user.getId());

            return checkVerifyCodeResponse;
        } else {
            throw new CustomException(ResponseCode.CODE_VERIFY_IS_INCORRECT);
        }
    }

    @Override
    public void checkPermission(String userId) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = findById(authentication.getName());
//        if (!userId.equals(currentUser.getId()) && !authUtils.isAdmin()) {
//            throw new CustomException(ErrorCode.UNAUTHORIZED);
//        }
    }

    @Override
    public User findById(String UserId) throws CustomException {
        var user = this.userRepository.findById(UserId);
        if (user.isEmpty()) {
            throw new CustomException(ResponseCode.USER_EXISTED);
        }

        return user.get();
    }

    @Override
    public User changeInfoAfterSignUp(String username, String currentUserId, String UserId, MultipartFile avatarFile) throws CustomException, IOException {
        var existedUser = this.findById(currentUserId);

        if (!Objects.equals(UserId, existedUser.getId())) {
            throw new CustomException(ResponseCode.PARAMETER_VALUE_IS_INVALID);
        }

        return this.updateUser(username, existedUser, avatarFile);
    }

    @Override
    public User resetPassword(ResetPasswordDTO resetPasswordDTO, String currentUserId, String userId) throws CustomException {
        var existedUser = this.findById(currentUserId);

//        if (!Objects.equals(userId, existedUser.getId())) {
//            throw new CustomException(ErrorCode.UNAUTHORIZED);
//        }

        existedUser.setPasswordHash(passwordEncoder.encode(resetPasswordDTO.getPassword()));

        return this.userRepository.save(existedUser);
    }

    private User updateUser(String username, User existedUser, MultipartFile avatarFile) throws IOException {

//        FileUtils fileUtils = new FileUtils();

//        existedUser.setGender(updateUserDTO.getGender() != null ? updateUserDTO.getGender() : existedUser.getGender());
//        existedUser.setBirthOfDate(updateUserDTO.getBirthOfDate() != null ? updateUserDTO.getBirthOfDate() : existedUser.getBirthOfDate());
//        existedUser.setMobile(updateUserDTO.getMobile() != null ? updateUserDTO.getMobile() : existedUser.getMobile());
        existedUser.setEmail(username != null ? username : existedUser.getEmail());
        existedUser.setUsername(username != null ? username : existedUser.getEmail());
//        existedUser.setFirstName(updateUserDTO.getFirstName() != null ? updateUserDTO.getFirstName() : existedUser.getFirstName());
//        existedUser.setLastName(updateUserDTO.getLastName() != null ? updateUserDTO.getLastName() : existedUser.getLastName());
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

    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    private boolean randomNumberExpired(int randomNumber) {
        int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
        int timestampCreated = randomNumber / 1000;

        return currentTimestamp - timestampCreated >= 5;
    }
}
