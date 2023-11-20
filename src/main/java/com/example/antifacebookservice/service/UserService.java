package com.example.antifacebookservice.service;

import com.example.antifacebookservice.constant.SettingStatus;
import com.example.antifacebookservice.controller.request.in.friendRequest.FriendRequestIn;
import com.example.antifacebookservice.controller.request.in.friendRequest.GetFriendRequest;
import com.example.antifacebookservice.controller.request.in.friendRequest.ProcessFriendRequest;
import com.example.antifacebookservice.controller.request.in.user.*;
import com.example.antifacebookservice.controller.request.in.setting.PushSettingIn;
import com.example.antifacebookservice.controller.request.in.version.CheckVersionIn;
import com.example.antifacebookservice.controller.request.out.friendRequest.FriendRequestOut;
import com.example.antifacebookservice.controller.request.out.friendRequest.GetRequestedFriendOutWrapper;
import com.example.antifacebookservice.controller.request.out.notification.UnreadNotificationOut;
import com.example.antifacebookservice.controller.request.out.user.SuggestedFriendOut;
import com.example.antifacebookservice.controller.request.out.user.UserInfoOut;
import com.example.antifacebookservice.controller.request.out.version.CheckVersionOut;
import com.example.antifacebookservice.controller.response.CheckVerifyCodeResponse;
import com.example.antifacebookservice.controller.response.GetCodeVerifyResponse;
import com.example.antifacebookservice.entity.Conversation;
import com.example.antifacebookservice.entity.Notification;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.List;

public interface UserService {
    User findByUsername(String username) throws CustomException;

    UserInfoOut getUserInfo(String token, String userId) throws CustomException;

    void createUser(SignUpDTO signUpDTO) throws CustomException, IOException, InterruptedException;

    GetCodeVerifyResponse getCodeVerify(String email) throws CustomException, IOException, InterruptedException;

    CheckVerifyCodeResponse checkVerifyCode(CheckCodeVerifyRequest checkCodeVerifyRequest) throws CustomException, IOException, InterruptedException;

    void checkPermission(String userId) throws CustomException;

    User findById(String UserId) throws CustomException;

    User changeInfoAfterSignUp(String token, UserDTO userDTO, MultipartFile avatarFile, MultipartFile coverImageFile) throws CustomException, IOException;

    GetRequestedFriendOutWrapper getRequestedFriend(GetFriendRequest getFriendRequest) throws CustomException;

    GetRequestedFriendOutWrapper getUserFriends(GetFriendRequest getFriendRequest) throws CustomException;

    User resetPassword(ResetPasswordDTO resetPasswordDTO, String currentUserId, String userId) throws CustomException;


    FriendRequestOut sendFriendRequest(FriendRequestIn friendRequestIn) throws CustomException;

    List<SuggestedFriendOut> getListSuggestedFriends(GetSuggestedFriends getSuggestedFriends) throws CustomException;

    Boolean setAcceptFriend(ProcessFriendRequest processFriendRequest) throws CustomException;

    Map<String, SettingStatus> getPushSetting(String token) throws CustomException;

    void setPushSetting(String token, PushSettingIn pushSettingIn) throws IllegalAccessException;

    CheckVersionOut checkNewVersion(CheckVersionIn checkVersionIn) throws CustomException;

    List<Notification> getNotification(String token, Integer index, Integer count) throws CustomException;

    UnreadNotificationOut setReadNotification(String token, String notificationId) throws CustomException;

    List<Conversation> getListConversation(String id) throws CustomException;

    Conversation getConversation(GetConversation getConversation) throws CustomException;

    Conversation setRead(GetConversation getConversation) throws CustomException;

    void deleteMessage(DeleteMessage deleteMessage) throws CustomException;

    void deleteConversation(DeleteConversation deleteConversation) throws CustomException;

    void setDevToken(String token, String devType, String devToken);
}
