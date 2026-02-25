package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.leyman.loco.locoback.domain.dto.ReactionDto;
import ru.leyman.loco.locoback.domain.repo.ReactionRepo;
import ru.leyman.loco.locoback.mapper.ReactionMapper;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepo reactionRepo;
    private final ReactionMapper reactionMapper;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public ReactionDto create(ReactionDto reactionDto) {
        var user = userService.getCurrentUser();
        var reaction = reactionMapper.map(reactionDto);
        reaction.setAuthor(user);
        if (Objects.nonNull(reactionDto.postId())) {
            var post = postService.get(reactionDto.postId());
            reaction.setPost(post);
        } else {
            var comment = commentService.get(reactionDto.commentId());
            reaction.setComment(comment);
        }
        return reactionMapper.map(reactionRepo.save(reaction));
    }

}
