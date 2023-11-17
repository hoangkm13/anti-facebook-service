package com.example.antifacebookservice.service.impl;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.in.comment.MarkCommentIn;
import com.example.antifacebookservice.controller.request.out.comment.MarkCommentOut;
import com.example.antifacebookservice.controller.request.out.user.AuthorOut;
import com.example.antifacebookservice.entity.Mark;
import com.example.antifacebookservice.entity.Post;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.helper.Common;
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

                int beginIndex = markCommentIn.getIndex();
                int endIndex = markCommentIn.getIndex() + markCommentIn.getCount() + 1;

                childComments = getChildComments(mark.get(), Sort.Direction.DESC, beginIndex, endIndex);

                return MarkCommentOut.builder()
                        .id(mark.get().getId())
                        .markContent(mark.get().getMarkContent())
                        .typeOfMark(mark.get().getTypeOfMark())
                        .poster(mapper.map(user, AuthorOut.class))
                        .comments(childComments)
                        .build();
            }
        }

        return MarkCommentOut.builder()
                .id(markOrComment.getId())
                .markContent(markOrComment.getMarkContent())
                .typeOfMark(markOrComment.getTypeOfMark())
                .poster(mapper.map(user, AuthorOut.class))
                .comments(childComments)
                .build();
    }

    @Override
    public MarkCommentOut getMarkComment(String id, int index, int count) throws CustomException {
//        User user = userService.findByUsername(DataContextHelper.getUserName());
//
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND, "Post not found!"));
//
//        if (post.isRestriction()) {
//            throw new CustomException(ResponseCode.RESTRICTION);
//        }
//
//        List<Mark> mark = markRepository.findByPostId(id);
//
//        List<MarkCommentOut> childComments = getChildComments(mark, Sort.Direction.DESC, index, count);
//
//        return MarkCommentOut.builder()
//                .id(mark.getId())
//                .markContent(mark.getMarkContent())
//                .poster(mapper.map(user, UserOut.class))
//                .typeOfMark(mark.getTypeOfMark())
//                .comments(childComments)
//                .build();
        return null;
    }

    private List<MarkCommentOut> getChildComments(Mark mark, Sort.Direction direction, int index, int count) throws CustomException {
        List<MarkCommentOut> childComments = new ArrayList<>();

        Sort sort = Sort.by(direction, "createdAt");
        List<Mark> marks = markRepository.findMarksByChildComments(mark.getChildComments(), sort);

        Common.checkValidIndexCount(index, count, marks.size());

        marks.subList(index, index + count + 1)
                .forEach(m -> {
                    try {
                        User commentPoster = userService.findById(DataContextHelper.getUserId());
                        childComments.add(MarkCommentOut.builder()
                                .id(m.getId())
                                .markContent(m.getMarkContent())
                                .poster(mapper.map(commentPoster, AuthorOut.class))
                                .build());
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                });

        return childComments;
    }
}

