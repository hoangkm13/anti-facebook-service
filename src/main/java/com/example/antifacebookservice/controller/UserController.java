package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.controller.request.in.friendRequest.FriendRequestIn;
import com.example.antifacebookservice.controller.request.in.friendRequest.ProcessFriendRequest;
import com.example.antifacebookservice.controller.request.in.user.GetSuggestedFriends;
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
    public ApiResponse<?> setBlock(@RequestBody List<String> blockIds) throws CustomException {
        blockService.setBlock(blockIds);
        return ApiResponse.successWithResult(null, "Block success!");
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
}
