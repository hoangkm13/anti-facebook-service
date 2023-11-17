package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.constant.SettingStatus;
import com.example.antifacebookservice.controller.request.auth.CheckCodeVerifyRequest;
import com.example.antifacebookservice.controller.request.auth.ResetPasswordDTO;
import com.example.antifacebookservice.controller.request.auth.SignUpDTO;
import com.example.antifacebookservice.controller.request.auth.in.setting.PushSettingIn;
import com.example.antifacebookservice.controller.request.auth.in.version.CheckVersionIn;
import com.example.antifacebookservice.controller.request.auth.out.user.UserVersionOut;
import com.example.antifacebookservice.controller.request.auth.out.version.CheckVersionOut;
import com.example.antifacebookservice.controller.response.CheckVerifyCodeResponse;
import com.example.antifacebookservice.controller.response.GetCodeVerifyResponse;
import com.example.antifacebookservice.entity.AppVersion;
import com.example.antifacebookservice.entity.CodeVerify;
import com.example.antifacebookservice.entity.PushSetting;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.helper.Common;
import com.example.antifacebookservice.repository.AppVersionRepository;
import com.example.antifacebookservice.repository.CodeVerifyRepository;
import com.example.antifacebookservice.repository.PushSettingRepository;
import com.example.antifacebookservice.repository.UserRepository;
import com.example.antifacebookservice.security.context.DataContextHelper;
import com.example.antifacebookservice.service.UserService;
import com.example.antifacebookservice.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final CodeVerifyRepository codeVerifyRepository;
    private final PushSettingRepository pushSettingRepository;
    private final AppVersionRepository appVersionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtils authUtils;

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
            throw new CustomException(ResponseCode.EXISTED);
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
            throw new CustomException(ResponseCode.EXISTED);
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
        ;
        if (passwordEncoder.matches(resetPasswordDTO.getCurrentPassword(), existedUser.getPasswordHash())) {
            existedUser.setPasswordHash(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        } else {
            throw new CustomException(ResponseCode.PARAMETER_VALUE_IS_INVALID, "Current password is wrong!");
        }

        return this.userRepository.save(existedUser);
    }

    @Override
    public Map<String, SettingStatus> getPushSetting(String token) throws CustomException {

        var currentSetting = pushSettingRepository.findByUserId(DataContextHelper.getUserId());

        if (currentSetting.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND, "Setting not found!");
        }

        return currentSetting.get().getSettings();
    }

    @Override
    public void setPushSetting(String token, PushSettingIn pushSettingIn) throws IllegalAccessException {
        PushSetting pushSetting = new PushSetting();
        pushSetting.setId(Common.generateUUID());
        pushSetting.setUserId(DataContextHelper.getUserId());

        var currentSetting = pushSettingRepository.findByUserId(DataContextHelper.getUserId());

        if (currentSetting.isPresent()) {
            pushSetting = currentSetting.get();
        }

        Map<String, SettingStatus> settingMap = new HashMap<>();

        Field[] fields = PushSettingIn.class.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);
            settingMap.put(f.getName(), (SettingStatus) f.get(pushSettingIn));
        }
        pushSetting.setSettings(settingMap);

        //Save push-setting
        pushSettingRepository.save(pushSetting);
    }

    @Override
    public CheckVersionOut checkNewVersion(CheckVersionIn checkVersionIn) throws CustomException {
        AppVersion appVersion = appVersionRepository.findAll(Sort.by(Sort.Direction.DESC, "releaseDate")).get(0);

        int compareResult = compareVersions(checkVersionIn.getLastUpdate(), appVersion.getVersionNumber());
        if (compareResult == 1) {
            UserVersionOut userVersionOut = new UserVersionOut();
            userVersionOut.setId(DataContextHelper.getUserId());
            userVersionOut.setActive(DataContextHelper.isUserActive());

            return CheckVersionOut.builder()
                    .appVersion(appVersion)
                    .user(userVersionOut)
                    .badge(0)
                    .unreadMessage(0)
                    .now("")
                    .build();
        } else if (compareResult == -1) {
            throw new CustomException(ResponseCode.SERVER_ERROR, "Something wrong!");
        }
        return null;
    }

    private int compareVersions(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");

        int length = Math.max(v1.length, v2.length);

        for (int i = 0; i < length; i++) {
            int num1 = (i < v1.length) ? Integer.parseInt(v1[i]) : 0;
            int num2 = (i < v2.length) ? Integer.parseInt(v2[i]) : 0;

            if (num1 < num2) {
                return 1;
            } else if (num1 > num2) {
                return -1;
            }
        }

        return 0; // versions are equal
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
