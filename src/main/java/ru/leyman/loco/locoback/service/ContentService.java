package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.leyman.loco.locoback.domain.entity.Content;
import ru.leyman.loco.locoback.domain.enums.ContentSize;
import ru.leyman.loco.locoback.domain.enums.ContentType;
import ru.leyman.loco.locoback.domain.repo.ContentRepo;
import ru.leyman.loco.locoback.domain.repo.PostRepo;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentService {

    private final FileService fileService;
    private final ContentRepo contentRepo;
    private final PostRepo postRepo;

    public Resource download(Long id, ContentSize size) {
        var content = contentRepo.getReferenceById(id);
        try {
            return switch (size) {
                case LARGE -> fileService.download(content.getLarge());
                case MEDIUM -> fileService.download(content.getMedium());
                case SMALL -> fileService.download(content.getSmall());
            };
        } catch (IOException e) {
            log.error("Error on downloading file with id={}", id);
            return null;
        }
    }

    public void upload(Long postId, ContentType type,
                       MultipartFile file) {
        var filename = fileService.upload(file, UUID.randomUUID().toString());
        var post = postRepo.getReferenceById(postId);
        var content = contentRepo.save(new Content(post, type, filename));
        contentRepo.save(content);
    }

}
