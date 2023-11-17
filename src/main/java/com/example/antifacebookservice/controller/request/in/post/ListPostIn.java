package com.example.antifacebookservice.controller.request.in.post;

import lombok.Data;

@Data
public class ListPostIn {
    private String token;

    private String userId;

    private String inCampaign;

    private String campaignId;

    private String latitude;

    private String longitude;

    private String lastId;

    private String index;

    private String count;

}