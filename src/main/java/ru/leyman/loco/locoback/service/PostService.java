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
        var post = postRepo.getReferenceById(id);
        var contents = contentRepo.findAllByPost(post);
        var reactions = reactionRepo.findAllByPost(post);
        return postMapper.mapToPreview(post, contents, reactions);
    }

    public PostDetails getDetails(Long id) {
        var post = postRepo.getReferenceById(id);
        var contents = contentRepo.findAllByPost(post);
        var reactions = reactionRepo.findAllByPost(post);
        var comments = commentRepo.findAllByPost(post);
        return postMapper.mapToDetails(post, contents, reactions, comments);
    }

    public PostDto create() {
        var post = new Post(userService.getCurrentUser());
        return postMapper.map(postRepo.save(post));
    }

    public PostDto update(PostDto postDto) {
        var post = postRepo.getReferenceById(postDto.id());
        var user = userService.getCurrentUser();
        if (!post.getAuthor().equals(user)) {
            throw new RuntimeException();
        }
        postMapper.map(post, postDto);
        post.setState(PostState.PUBLISHED);
        return postMapper.map(postRepo.save(post));
    }

    public Post get(Long id) {
        return postRepo.getReferenceById(id);
    }

}
