package com.example.antifacebookservice.service;

import com.example.antifacebookservice.controller.request.in.user.BlockUserIn;
import com.example.antifacebookservice.controller.request.out.user.BlockUserOut;
import com.example.antifacebookservice.exception.CustomException;

import java.util.List;

public interface BlockService {
    String setBlock(BlockUserIn blockUserIn) throws CustomException;

    List<BlockUserOut> getListBlocked(String token, Integer index, Integer count) throws CustomException;
}
