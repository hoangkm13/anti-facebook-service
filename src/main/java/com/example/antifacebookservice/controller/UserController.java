package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.controller.request.auth.out.user.BlockUserOut;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.model.ApiResponse;
import com.example.antifacebookservice.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user")
@CrossOrigin(origins = "http://localhost:8029", allowCredentials = "true")
@RequiredArgsConstructor
public class UserController {
    private final BlockService blockService;

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
}
