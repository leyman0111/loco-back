package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.leyman.loco.locoback.domain.dto.CommentDto;
import ru.leyman.loco.locoback.domain.entity.Comment;
import ru.leyman.loco.locoback.domain.repo.CommentRepo;
import ru.leyman.loco.locoback.mapper.CommentMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PostService postService;

    public CommentDto create(CommentDto commentDto) {
        var user = userService.getCurrentUser();
        var post = postService.get(commentDto.postId());
        var comment = commentMapper.map(commentDto);
        comment.setAuthor(user);
        comment.setPost(post);
        return commentMapper.map(commentRepo.save(comment));
    }

    public Comment get(Long id) {
        return commentRepo.getReferenceById(id);
    }

}
