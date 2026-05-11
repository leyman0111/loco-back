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
import java.util.Map;
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
    private final FcmService fcmService;

    // Emoji map for reaction types (adjust keys to match your ReactionType enum)
    private static final Map<String, String> REACTION_EMOJI = Map.of(
            "FIRE", "🔥",
            "HEART", "❤️",
            "LIKE", "👍",
            "WOW", "😮",
            "LAUGH", "😂",
            "SAD", "😢"
    );

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

    public void delete(Long id) {
        reactionRepo.deleteById(id);
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
                .ifPresent(post -> {
                    reaction.setPost(post);
                    // Send push to post author (skip self-reaction)
                    var postAuthor = post.getAuthor();
                    if (postAuthor != null && !postAuthor.getId().equals(author.getId())) {
                        var authorName = author.getUsername() != null ? author.getUsername() : "Кто-то";
                        var emoji = reactionDto.type() != null
                                ? REACTION_EMOJI.getOrDefault(reactionDto.type().name(), "👍")
                                : "👍";
                        fcmService.sendToUser(postAuthor,
                                authorName + " отреагировал на ваш пост",
                                emoji + " " + authorName + " поставил реакцию на ваш пост");
                    }
                });
        Optional.ofNullable(reactionDto.commentId())
                .map(commentService::get)
                .ifPresent(reaction::setComment);
        return reactionMapper.map(reactionRepo.save(reaction));
    }

}
