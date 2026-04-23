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
import ru.leyman.loco.locoback.utils.FilenameUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static java.util.Objects.requireNonNullElse;
import static ru.leyman.loco.locoback.domain.enums.ContentSize.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentService {

    private final FileService fileService;
    private final ContentRepo contentRepo;
    private final PostService postService;
    private final UserService userService;
    private final ImageService imageService;
    private final ExecutorService executorService;

    public Resource download(Long id, ContentSize size) {
        var content = contentRepo.getReferenceById(id);
        if (Objects.isNull(size)) {
            return fileService.download(content.getOrigin());
        }
        return switch (size) {
            case LARGE -> fileService.download(requireNonNullElse(content.getLarge(), content.getOrigin()));
            case MEDIUM -> fileService.download(requireNonNullElse(content.getMedium(), content.getOrigin()));
            case SMALL -> fileService.download(requireNonNullElse(content.getSmall(), content.getOrigin()));
        };
    }

    public void upload(Long postId, ContentType type, MultipartFile file) {
        var post = postService.get(postId);
        if (!PostState.CREATED.equals(post.getState())) {
            throw new RuntimeException();
        }
        var user = userService.getCurrentUser();
        if (!post.getAuthor().equals(user)) {
            throw new RuntimeException();
        }
        var filename = FilenameUtils.replaceFilename(Objects.requireNonNull(file.getOriginalFilename()),
                UUID.randomUUID().toString());
        var path = fileService.upload(file, filename);
        var content = contentRepo.save(new Content(post, type, path.toString()));

        if (ContentType.IMAGE == type) {
            var small = executorService.submit(() -> imageService.resize(filename, path, SMALL));
            var medium = executorService.submit(() -> imageService.resize(filename, path, MEDIUM));
            var large = executorService.submit(() -> imageService.resize(filename, path, LARGE));
            try {
                content.setSmall(small.get());
                content.setMedium(medium.get());
                content.setLarge(large.get());
            } catch (ExecutionException | InterruptedException e) {
                log.error("Can't resize image", e);
            }
            contentRepo.save(content);
        }
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
