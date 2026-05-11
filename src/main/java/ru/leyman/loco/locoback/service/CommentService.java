package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.leyman.loco.locoback.domain.dto.CommentDto;
import ru.leyman.loco.locoback.domain.entity.Comment;
import ru.leyman.loco.locoback.domain.repo.CommentRepo;
import ru.leyman.loco.locoback.mapper.CommentMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PostService postService;
    private final FcmService fcmService;

    public CommentDto create(CommentDto commentDto) {
        var user = userService.getCurrentUser();
        var post = postService.get(commentDto.postId());
        var comment = commentMapper.map(commentDto);
        comment.setAuthor(user);
        comment.setPost(post);
        var saved = commentMapper.map(commentRepo.save(comment));

        // Send push notification to post author (skip if author comments their own post)
        var postAuthor = post.getAuthor();
        if (postAuthor != null && !postAuthor.getId().equals(user.getId())) {
            var authorName = user.getUsername() != null ? user.getUsername() : "Кто-то";
            var preview = commentDto.text() != null && commentDto.text().length() > 60
                    ? commentDto.text().substring(0, 60) + "..."
                    : commentDto.text();
            fcmService.sendToUser(postAuthor,
                    authorName + " прокомментировал ваш пост",
                    preview);
        }

        return saved;
    }

    public Comment get(Long id) {
        return commentRepo.getReferenceById(id);
    }

    public List<CommentDto> getByPost(Long postId) {
        var post = postService.get(postId);
        return commentRepo.findAllByPost(post).stream()
                .map(commentMapper::map)
                .toList();
    }

}
