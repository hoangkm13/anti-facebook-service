package com.example.antifacebookservice.controller.request.out.friendRequest;

import com.example.antifacebookservice.controller.request.out.user.BaseUserOut;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetRequestedFriendOutBase extends BaseUserOut {
    private Integer sameFriends;
}
