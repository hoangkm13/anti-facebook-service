package com.example.antifacebookservice.controller.request.auth.in.setting;

import com.example.antifacebookservice.constant.SettingStatus;
import lombok.Data;

@Data
public class PushSettingIn {
    private SettingStatus likeComment;
    private SettingStatus fromFriends;
    private SettingStatus requestedFriends;
    private SettingStatus suggestedFriends;
    private SettingStatus birthDay;
    private SettingStatus video;
    private SettingStatus report;
    private SettingStatus soundOn;
    private SettingStatus notificationOn;
    private SettingStatus vibrantOn;
    private SettingStatus ledOn;
}
