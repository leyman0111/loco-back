package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.leyman.loco.locoback.domain.dto.*;
import ru.leyman.loco.locoback.domain.entity.Post;
import ru.leyman.loco.locoback.domain.enums.PostCategory;
import ru.leyman.loco.locoback.domain.enums.PostState;
import ru.leyman.loco.locoback.domain.repo.CommentRepo;
import ru.leyman.loco.locoback.domain.repo.ContentRepo;
import ru.leyman.loco.locoback.domain.repo.PostRepo;
import ru.leyman.loco.locoback.domain.repo.ReactionRepo;
import ru.leyman.loco.locoback.mapper.PostMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepo postRepo;
    private final PostMapper postMapper;
    private final ContentRepo contentRepo;
    private final ReactionRepo reactionRepo;
    private final CommentRepo commentRepo;
    private final UserService userService;

    public List<PostMark> getByScope(Scope scope) {
        var categories = CollectionUtils.isEmpty(scope.categories()) ? PostCategory.ALL : scope.categories();
        return postRepo.getPostByLocation(scope.latitude(), scope.longitude(), scope.distance(), categories).stream()
                .map(postMapper::mapToMark)
                .toList();
    }

    public PostPreview getPreview(Long id) {
        var post = postRepo.findByIdAndState(id, PostState.PUBLISHED).orElseThrow();
        var contents = contentRepo.findAllByPost(post);
        var reactions = reactionRepo.findAllByPost(post);
        return postMapper.mapToPreview(post, contents, reactions);
    }

    public PostDetails getDetails(Long id) {
        var post = postRepo.findByIdAndState(id, PostState.PUBLISHED).orElseThrow();
        var contents = contentRepo.findAllByPost(post);
        var reactions = reactionRepo.findAllByPost(post);
        var comments = commentRepo.findAllByPost(post);
        return postMapper.mapToDetails(post, contents, reactions, comments);
    }

    public PostDto create() {
        var user = userService.getCurrentUser();
        return postRepo.findAllByAuthorAndState(user, PostState.CREATED).stream().findFirst()
                .map(post -> {
                    var contents = contentRepo.findAllByPost(post);
                    return postMapper.map(post, contents);
                })
                .orElseGet(() -> {
                    var post = postRepo.save(new Post(user));
                    return postMapper.map(post);
                });
    }

    public PostDto update(PostDto postDto) {
        var user = userService.getCurrentUser();
        return postRepo.findAllByAuthorAndState(user, PostState.CREATED).stream()
                .filter(post -> post.getId().equals(postDto.id())).findFirst()
                .map(post -> {
                    postMapper.map(post, postDto);
                    post.setState(PostState.PUBLISHED);
                    return postMapper.map(postRepo.save(post));
                })
                .orElseThrow();
    }

    public Post get(Long id) {
        return postRepo.getReferenceById(id);
    }

}
