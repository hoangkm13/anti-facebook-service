package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.auth.out.user.BlockUserOut;
import com.example.antifacebookservice.entity.BlockUser;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.helper.Common;
import com.example.antifacebookservice.repository.BlockRepository;
import com.example.antifacebookservice.repository.UserRepository;
import com.example.antifacebookservice.security.context.DataContextHelper;
import com.example.antifacebookservice.service.BlockService;
import com.example.antifacebookservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {
    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    @Override
    public void setBlock(List<String> userIds) throws CustomException {
        String currentUserId = DataContextHelper.getUserId();
        List<BlockUser> blockUsers = new ArrayList<>();
        for (String id : userIds) {
            if (userRepository.existsById(id) && !blockRepository.existsByBlockerIdAndBlockedId(currentUserId, id)) {
                blockUsers.add(BlockUser.builder()
                        .blockerId(currentUserId)
                        .blockedId(id)
                        .build());
            } else {
                throw new CustomException(ResponseCode.NOT_FOUND, "User not found!");
            }
        }

        blockRepository.saveAll(blockUsers);
    }

    @Override
    public List<BlockUserOut> getListBlocked(String token, Integer index, Integer count) throws CustomException {
        String userId = DataContextHelper.getUserId();

        List<String> blockedIds = blockRepository.findAllByBlockerId(userId)
                .stream().map(BlockUser::getBlockedId).collect(Collectors.toList());

        Common.checkValidIndexCount(index, count, blockedIds.size());

        List<BlockUserOut> blockUserOuts = new ArrayList<>();
        for (String id : blockedIds) {
            User user = userService.findById(id);
            blockUserOuts.add(mapper.map(user, BlockUserOut.class));
        }

        return blockUserOuts;
    }
}
