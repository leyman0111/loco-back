package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.leyman.loco.locoback.domain.dto.ReactionDto;
import ru.leyman.loco.locoback.domain.entity.Reaction;
import ru.leyman.loco.locoback.domain.entity.User;
import ru.leyman.loco.locoback.domain.repo.ReactionRepo;
import ru.leyman.loco.locoback.mapper.ReactionMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepo reactionRepo;
    private final ReactionMapper reactionMapper;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public ReactionDto upsert(ReactionDto reactionDto) {
        var user = userService.getCurrentUser();
        return this.find(reactionDto, user.getId())
                .map(reaction -> update(reaction, reactionDto))
                .orElseGet(() -> create(reactionDto, user));
    }

    public List<ReactionDto> getByPost(Long postId) {
        var post = postService.get(postId);
        return reactionRepo.findAllByPost(post).stream()
                .map(reactionMapper::map)
                .toList();
    }

    private Optional<Reaction> find(ReactionDto reactionDto, Long authorId) {
        return Objects.nonNull(reactionDto.postId())
                ? reactionRepo.findByPostIdAndAuthorId(reactionDto.postId(), authorId)
                : reactionRepo.findByCommentIdAndAuthorId(reactionDto.commentId(), authorId);
    }

    private ReactionDto update(Reaction reaction, ReactionDto reactionDto) {
        reaction.setType(reactionDto.type());
        return reactionMapper.map(reactionRepo.save(reaction));
    }

    private ReactionDto create(ReactionDto reactionDto, User author) {
        var reaction = reactionMapper.map(reactionDto);
        reaction.setAuthor(author);
        Optional.ofNullable(reactionDto.postId())
                .map(postService::get)
                .ifPresent(reaction::setPost);
        Optional.ofNullable(reactionDto.commentId())
                .map(commentService::get)
                .ifPresent(reaction::setComment);
        return reactionMapper.map(reactionRepo.save(reaction));
    }

}
