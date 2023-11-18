package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.constant.SettingStatus;
import com.example.antifacebookservice.controller.request.in.friendRequest.FriendRequestIn;
import com.example.antifacebookservice.controller.request.in.friendRequest.GetFriendRequest;
import com.example.antifacebookservice.controller.request.in.friendRequest.ProcessFriendRequest;
import com.example.antifacebookservice.controller.request.in.setting.PushSettingIn;
import com.example.antifacebookservice.controller.request.in.user.*;
import com.example.antifacebookservice.controller.request.in.version.CheckVersionIn;
import com.example.antifacebookservice.controller.request.out.friendRequest.FriendRequestOut;
import com.example.antifacebookservice.controller.request.out.friendRequest.GetRequestedFriendOut;
import com.example.antifacebookservice.controller.request.out.friendRequest.GetRequestedFriendOutWrapper;
import com.example.antifacebookservice.controller.request.out.user.SuggestedFriendOut;
import com.example.antifacebookservice.controller.request.out.user.UserOut;
import com.example.antifacebookservice.controller.request.out.user.UserVersionOut;
import com.example.antifacebookservice.controller.request.out.version.CheckVersionOut;
import com.example.antifacebookservice.controller.response.CheckVerifyCodeResponse;
import com.example.antifacebookservice.controller.response.GetCodeVerifyResponse;
import com.example.antifacebookservice.entity.*;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.helper.Common;
import com.example.antifacebookservice.repository.*;
import com.example.antifacebookservice.security.context.DataContextHelper;
import com.example.antifacebookservice.service.UserService;
import com.example.antifacebookservice.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
    private final FriendRequestRepository friendRequestRepository;
    private final ModelMapper modelMapper;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

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
            throw new CustomException(ResponseCode.NOT_EXISTED);
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
    public FriendRequestOut sendFriendRequest(FriendRequestIn friendRequestIn) throws CustomException {
        findById(friendRequestIn.getUserReceiveId());

        var existedFriendRequest = this.friendRequestRepository.findByUserSentIdAndUserReceiveId(DataContextHelper.getUserId(), friendRequestIn.getUserReceiveId());

        if (existedFriendRequest != null) {
            throw new CustomException(ResponseCode.FRIEND_REQUEST_EXISTED);
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setUserSentId(DataContextHelper.getUserId());
        friendRequest.setUserReceiveId(friendRequestIn.getUserReceiveId());
        friendRequest.setIsAccepted(false);
        friendRequest.setCreatedAt(LocalDateTime.now().toString());

        this.friendRequestRepository.save(friendRequest);

        var countFriendRequest = this.friendRequestRepository.countFriendRequestByUserSentId(DataContextHelper.getUserId());

        var result = FriendRequestOut.builder().build();
        result.setNumberOfPendingFriendRequest(countFriendRequest);
        return result;
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

    @Override
    public List<Conversation> getListConversation(String id) throws CustomException {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID().toString());

        conversation.setPartner(this.modelMapper.map(this.findById("e1642fd2-1512-4e16-aa2c-8e5a5bc333ee"), UserOut.class));
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setContent("test " + LocalDateTime.now().toString());
        message.setUnread(new Random().nextBoolean());
        message.setCreated(LocalDateTime.now().toString());

        conversation.setLastMessage(message);
        this.messageRepository.save(message);

        this.conversationRepository.save(conversation);

        return this.conversationRepository.findAll();
    }

    @Override
    public Conversation getConversation(GetConversation getConversation) throws CustomException {
        return this.conversationRepository.findById(getConversation.getId()).get();
    }

    @Override
    public Conversation setRead(GetConversation getConversation) throws CustomException {
        var conv = this.conversationRepository.findById(getConversation.getId()).get();
        conv.getLastMessage().setUnread(true);

        return conv;
    }

    @Override
    public void deleteMessage(DeleteMessage deleteMessage) throws CustomException {
        this.messageRepository.deleteById(deleteMessage.getId());
    }

    @Override
    public void deleteConversation(DeleteConversation deleteConversation) throws CustomException {
        this.messageRepository.deleteById(deleteConversation.getId());
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

    @Override
    public List<SuggestedFriendOut> getListSuggestedFriends(GetSuggestedFriends getSuggestedFriends) throws CustomException {
        List<User> allUsers = this.userRepository.findAll();
        allUsers.removeIf(a -> Objects.equals(a.getId(), DataContextHelper.getUserId()));

        Common.checkValidIndexCount(getSuggestedFriends.getCount(), getSuggestedFriends.getIndex(), allUsers.size());

        var indexed = allUsers.subList(getSuggestedFriends.getIndex(), getSuggestedFriends.getIndex() + getSuggestedFriends.getCount());

        List<SuggestedFriendOut> recommendFriends = new ArrayList<>();
        for (User user : indexed) {
            if (!CollectionUtils.isEmpty(user.getFriendLists())) {
                SuggestedFriendOut suggestedFriendOut = new SuggestedFriendOut();
                int i = 0;
                for (String mutualFriendId : user.getFriendLists()) {
                    if (findById(DataContextHelper.getUserId()).getFriendLists().contains(mutualFriendId)) {
                        i++;
                    }
                }
                if (i > 0) {
                    suggestedFriendOut.setSameFriends(i);
                    this.modelMapper.map(user, suggestedFriendOut);
                    recommendFriends.add(suggestedFriendOut);
                }
            }

        }
        return recommendFriends;
    }

    @Override
    public GetRequestedFriendOutWrapper getRequestedFriend(GetFriendRequest getFriendRequest) throws CustomException {
        List<FriendRequest> friendRequests = this.friendRequestRepository.findByUserReceiveIdAndIsAccepted(DataContextHelper.getUserId(), false);

        Common.checkValidIndexCount(getFriendRequest.getCount(), getFriendRequest.getIndex(), friendRequests.size());

        var indexed = friendRequests.subList(getFriendRequest.getIndex(), getFriendRequest.getIndex() + getFriendRequest.getCount());

        List<String> listUserIds = new ArrayList<>();
        for (FriendRequest friendRequest : indexed) {
            listUserIds.add(friendRequest.getUserSentId());
        }

        var users = this.userRepository.findAllById(listUserIds);

        List<GetRequestedFriendOut> getRequestedFriendOutList = new ArrayList<>();

        users.removeIf(a -> Objects.equals(a.getId(), DataContextHelper.getUserId()));

        for (User user : users) {
            if (!CollectionUtils.isEmpty(user.getFriendLists())) {
                GetRequestedFriendOut getRequestedFriendOut = new GetRequestedFriendOut();
                int i = 0;
                for (String mutualFriendId : user.getFriendLists()) {
                    if (findById(DataContextHelper.getUserId()).getFriendLists().contains(mutualFriendId)) {
                        i++;
                    }
                }
                if (i > 0) {
                    getRequestedFriendOut.setSameFriends(i);
                    this.modelMapper.map(user, getRequestedFriendOut);
                    getRequestedFriendOutList.add(getRequestedFriendOut);
                }
            }
        }

        GetRequestedFriendOutWrapper getRequestedFriendOutWrapper = new GetRequestedFriendOutWrapper();
        getRequestedFriendOutWrapper.setRequest(getRequestedFriendOutList);
        getRequestedFriendOutWrapper.setTotal(listUserIds.size());

        return getRequestedFriendOutWrapper;
    }

    @Override
    public GetRequestedFriendOutWrapper getUserFriends(GetFriendRequest getFriendRequest) throws CustomException {
        List<User> result = this.userRepository.findAllById(userRepository.findById(DataContextHelper.getUserId()).get().getFriendLists());

        Common.checkValidIndexCount(getFriendRequest.getCount(), getFriendRequest.getIndex(), result.size());

        var indexed = result.subList(getFriendRequest.getIndex(), getFriendRequest.getIndex() + getFriendRequest.getCount());

//        var users = this.userRepository.findAllById(userRepository.findById(DataContextHelper.getUserId()).get().getFriendLists());
//
//        users.removeIf(a -> Objects.equals(a.getId(), DataContextHelper.getUserId()));
//
//        List<GetRequestedFriendOut> getRequestedFriendOutList = new ArrayList<>();
//
//        for (User user : indexed) {
//            if (!CollectionUtils.isEmpty(user.getFriendLists())) {
//                GetRequestedFriendOut getRequestedFriendOut = new GetRequestedFriendOut();
//                int i = 0;
//                for (String mutualFriendId https://code-with-me.global.jetbrains.com/DiSn9OP4lehWwXOweyHxwQ#p=IU&fp=175D1F9F07492FD6D937EDF5BE96D90EEBA76538BC70EF78CD7C8B370A3F804B&newUi=true: user.getFriendLists()) {
//                    if (findById(DataContextHelper.getUserId()).getFriendLists().contains(mutualFriendId)) {
//                        i++;
//                    }
//                }
//                if (i > 0) {
//                    getRequestedFriendOut.setSameFriends(i);
//                    this.modelMapper.map(user, getRequestedFriendOut);
//                    getRequestedFriendOutList.add(getRequestedFriendOut);
//                }
//            }
//        }
        List<GetRequestedFriendOut> getRequestedFriendOutList = new ArrayList<>();

        for (User user : indexed) {
            getRequestedFriendOutList.add(this.modelMapper.map(user, GetRequestedFriendOut.class));
        }

        GetRequestedFriendOutWrapper getRequestedFriendOutWrapper = new GetRequestedFriendOutWrapper();
        getRequestedFriendOutWrapper.setRequest(getRequestedFriendOutList);
        getRequestedFriendOutWrapper.setTotal(indexed.size());

        return getRequestedFriendOutWrapper;
    }


    @Override
    public Boolean setAcceptFriend(ProcessFriendRequest processFriendRequest) throws CustomException {
        var userSent = findById(processFriendRequest.getUserSentId());
        var userReceive = findById(DataContextHelper.getUserId());

        var existedFriendRequest = this.friendRequestRepository.findByUserSentIdAndUserReceiveId(processFriendRequest.getUserSentId(), DataContextHelper.getUserId());

        if (existedFriendRequest == null) {
            throw new CustomException(ResponseCode.FRIEND_REQUEST_NOT_EXISTED);
        }

        existedFriendRequest.setIsAccepted(processFriendRequest.getIsAccept());

        this.friendRequestRepository.save(existedFriendRequest);

        if (existedFriendRequest.getIsAccepted()) {
            if (CollectionUtils.isEmpty(userSent.getFriendLists())) {
                List<String> newFriends = new ArrayList<>();
                newFriends.add(DataContextHelper.getUserId());

                userSent.setFriendLists(newFriends);
            } else {
                userSent.getFriendLists().add(DataContextHelper.getUserId());
            }

            this.userRepository.save(userSent);

            if (CollectionUtils.isEmpty(userReceive.getFriendLists())) {
                List<String> newFriends = new ArrayList<>();
                newFriends.add(processFriendRequest.getUserSentId());

                userReceive.setFriendLists(newFriends);
            } else {
                userReceive.getFriendLists().add(processFriendRequest.getUserSentId());
            }

            this.userRepository.save(userReceive);
        }

        return existedFriendRequest.getIsAccepted();
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
