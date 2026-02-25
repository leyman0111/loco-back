package ru.leyman.loco.locoback.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.leyman.loco.locoback.domain.enums.ContentSize;
import ru.leyman.loco.locoback.domain.enums.ContentType;
import ru.leyman.loco.locoback.service.ContentService;

@RestController
@RequestMapping("contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService service;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody Resource download(@PathVariable Long id, @RequestParam ContentSize size) {
        return service.download(id, size);
    }

    @PostMapping("{postId}")
    public void upload(@PathVariable Long postId, @RequestParam ContentType type,
                       @RequestBody MultipartFile file) {
        service.upload(postId, type, file);
    }

}
