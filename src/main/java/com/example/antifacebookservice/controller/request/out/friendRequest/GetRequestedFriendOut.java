package com.example.antifacebookservice.controller.request.out.friendRequest;

import com.example.antifacebookservice.controller.request.out.user.UserOut;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetRequestedFriendOut extends UserOut {
    private Integer sameFriends;
}
