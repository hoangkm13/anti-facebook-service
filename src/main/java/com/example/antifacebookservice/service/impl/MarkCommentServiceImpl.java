package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.auth.in.comment.MarkCommentIn;
import com.example.antifacebookservice.controller.request.auth.out.comment.MarkCommentOut;
import com.example.antifacebookservice.controller.request.auth.out.post.UserOut;
import com.example.antifacebookservice.entity.Mark;
import com.example.antifacebookservice.entity.Post;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.repository.MarkRepository;
import com.example.antifacebookservice.repository.PostRepository;
import com.example.antifacebookservice.security.context.DataContextHelper;
import com.example.antifacebookservice.service.MarkCommentService;
import com.example.antifacebookservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarkCommentServiceImpl implements MarkCommentService {

    private final MarkRepository markRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    @Override
    public MarkCommentOut setMarkComment(MarkCommentIn markCommentIn) throws CustomException {
        User user = userService.findByUsername(DataContextHelper.getUserName());

        Post post = postRepository.findById(markCommentIn.getId())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));

        if (post.isRestriction()) {
            throw new CustomException(ResponseCode.RESTRICTION);
        }

        Mark markOrComment = new Mark();

        markOrComment.setId(UUID.randomUUID().toString());
        markOrComment.setMarkContent(markCommentIn.getContent());
        markOrComment.setCreatedAt(LocalDateTime.now().toString());
        markOrComment.setUserId(DataContextHelper.getUserId());
        markOrComment.setPostId(markCommentIn.getId());
        markOrComment.setChildComments(new ArrayList<>());

        markRepository.save(markOrComment);

        List<MarkCommentOut> childComments = new ArrayList<>();

        if (!StringUtils.isEmpty(markCommentIn.getMarkId())) {
            Optional<Mark> mark = markRepository.findById(markCommentIn.getMarkId());

            if (mark.isPresent()) {
                mark.get().setTypeOfMark(markCommentIn.getType());
                mark.get().getChildComments().add(markOrComment.getId());
                markRepository.save(mark.get());

                Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

                int beginIndex = markCommentIn.getIndex();
                int endIndex =markCommentIn.getIndex() + markCommentIn.getCount() + 1;
                markRepository.findMarksByChildComments(mark.get().getChildComments(), sort)
                        .subList(beginIndex, endIndex)
                        .forEach(m -> {
                            try {
                                User commentPoster = userService.findById(DataContextHelper.getUserId());
                                childComments.add(MarkCommentOut.builder()
                                        .id(m.getId())
                                        .markContent(m.getMarkContent())
                                        .poster(mapper.map(commentPoster, UserOut.class))
                                        .build());
                            } catch (CustomException e) {
                                throw new RuntimeException(e);
                            }
                        });

                return MarkCommentOut.builder()
                        .id(mark.get().getId())
                        .markContent(mark.get().getMarkContent())
                        .typeOfMark(mark.get().getTypeOfMark())
                        .poster(mapper.map(user, UserOut.class))
                        .comments(childComments)
                        .build();
            }
        }

        return MarkCommentOut.builder()
                .id(markOrComment.getId())
                .markContent(markOrComment.getMarkContent())
                .typeOfMark(markOrComment.getTypeOfMark())
                .poster(mapper.map(user, UserOut.class))
                .comments(childComments)
                .build();
    }

    @Override
    public MarkCommentOut getMarkComment(String token, String id, int count) throws CustomException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));

        if (post.isRestriction()) {
            throw new CustomException(ResponseCode.RESTRICTION);
        }

        return null;
    }
}

