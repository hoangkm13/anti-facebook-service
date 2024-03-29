package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.controller.request.in.friendRequest.FriendRequestIn;
import com.example.antifacebookservice.controller.request.in.friendRequest.GetFriendRequest;
import com.example.antifacebookservice.controller.request.in.friendRequest.ProcessFriendRequest;
import com.example.antifacebookservice.controller.request.in.setting.PushSettingIn;
import com.example.antifacebookservice.controller.request.in.user.*;
import com.example.antifacebookservice.controller.request.in.version.CheckVersionIn;
import com.example.antifacebookservice.controller.request.out.user.BlockUserOut;
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


    @PostMapping("/send-friend-request")
    public ApiResponse<?> sendFriendRequest(@RequestBody FriendRequestIn friendRequestIn) throws CustomException {
        var result = userService.sendFriendRequest(friendRequestIn);
        return ApiResponse.successWithResult(result);
    }

    @PostMapping("/set-accept-friend")
    public ApiResponse<?> setAcceptFriend(@RequestBody ProcessFriendRequest processFriendRequest) throws CustomException {
        var result = userService.setAcceptFriend(processFriendRequest);
        return ApiResponse.successWithResult(result);
    }

    @PostMapping("/get-list-suggested-friends")
    public ApiResponse<?> getListSuggestedFriends(@RequestBody GetSuggestedFriends getSuggestedFriends) throws CustomException {
        var result = userService.getListSuggestedFriends(getSuggestedFriends);
        return ApiResponse.successWithResult(result);
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


    @PostMapping("/get-requested-friends")
    public ApiResponse<?> getRequestedFriend(@RequestBody GetFriendRequest getFriendRequest) throws CustomException {
        return ApiResponse.successWithResult(userService.getRequestedFriend(getFriendRequest));
    }

    @PostMapping("/get-user-friends")
    public ApiResponse<?> getUserFriends(@RequestBody GetFriendRequest getFriendRequest) throws CustomException {
        return ApiResponse.successWithResult(userService.getUserFriends(getFriendRequest));
    }

    @PostMapping("/get-list-conversations")
    public ApiResponse<?> getListConversation(@RequestBody String token, String index, String count) throws CustomException {
        return ApiResponse.successWithResult(userService.getListConversation(token));
    }

    @PostMapping("/get-conversation")
    public ApiResponse<?> getConversation(@RequestBody GetConversation getConversation) throws CustomException {
        return ApiResponse.successWithResult(userService.getConversation(getConversation));
    }

    @PostMapping("/set-read-message")
    public ApiResponse<?> setRead(@RequestBody GetConversation getConversation) throws CustomException {
        return ApiResponse.successWithResult(userService.setRead(getConversation));
    }

    @PostMapping("/delete-message")
    public ApiResponse<?> deleteMessage(@RequestBody DeleteMessage deleteMessage) throws CustomException {

        userService.deleteMessage(deleteMessage);
        return ApiResponse.successWithResult(null, "Delete success!");
    }

    @PostMapping("/delete-conversation")
    public ApiResponse<?> deleteMessage(@RequestBody DeleteConversation deleteConversation) throws CustomException {
        userService.deleteConversation(deleteConversation);
        return ApiResponse.successWithResult(null, "Delete success!");
    }

    @PostMapping("/get-notification")
    public ApiResponse<?> getNotification(String token, Integer index, Integer count) throws CustomException {
        return ApiResponse.successWithResult(userService.getNotification(token, index, count));
    }

    @PostMapping("/set-read-notification")
    public ApiResponse<?> setReadNotification(String token, String notificationId) throws CustomException {
        return ApiResponse.successWithResult(userService.setReadNotification(token, notificationId));
    }

    @PostMapping("/set-devtoken")
    public ApiResponse<?> setDevToken(String token, Integer devType, String devToken) {
        userService.setDevToken(token, devType, devToken);
        return ApiResponse.successWithResult(null);
    }


}
