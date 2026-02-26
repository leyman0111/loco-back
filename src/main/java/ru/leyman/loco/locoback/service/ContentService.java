package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.leyman.loco.locoback.domain.entity.Content;
import ru.leyman.loco.locoback.domain.enums.ContentSize;
import ru.leyman.loco.locoback.domain.enums.ContentType;
import ru.leyman.loco.locoback.domain.enums.PostState;
import ru.leyman.loco.locoback.domain.repo.ContentRepo;

import java.io.IOException;

import static java.util.Objects.requireNonNullElse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentService {

    private final FileService fileService;
    private final ContentRepo contentRepo;
    private final PostService postService;
    private final UserService userService;

    public Resource download(Long id, ContentSize size) {
        var content = contentRepo.getReferenceById(id);
        try {
            return switch (size) {
                case LARGE -> fileService.download(requireNonNullElse(content.getLarge(), content.getOrigin()));
                case MEDIUM -> fileService.download(requireNonNullElse(content.getMedium(), content.getOrigin()));
                case SMALL -> fileService.download(requireNonNullElse(content.getSmall(), content.getOrigin()));
            };
        } catch (IOException e) {
            log.error("Error on downloading file with id={}", id);
            return null;
        }
    }

    public void upload(Long postId, ContentType type,
                       MultipartFile file) {
        var post = postService.get(postId);
        if (!PostState.CREATED.equals(post.getState())) {
            throw new RuntimeException();
        }
        var user = userService.getCurrentUser();
        if (!post.getAuthor().equals(user)) {
            throw new RuntimeException();
        }
        var filename = fileService.upload(file);
        var content = contentRepo.save(new Content(post, type, filename));
        contentRepo.save(content);
    }

    public void delete(Long id) {
        var content = contentRepo.getReferenceById(id);
        var post = content.getPost();
        if (!PostState.CREATED.equals(post.getState())) {
            throw new RuntimeException();
        }
        var user = userService.getCurrentUser();
        if (!post.getAuthor().equals(user)) {
            throw new RuntimeException();
        }
        contentRepo.delete(content);
        fileService.delete(content.getOrigin());
        if (StringUtils.hasText(content.getLarge()))
            fileService.delete(content.getLarge());
        if (StringUtils.hasText(content.getMedium()))
            fileService.delete(content.getMedium());
        if (StringUtils.hasText(content.getSmall()))
            fileService.delete(content.getSmall());
    }

}
