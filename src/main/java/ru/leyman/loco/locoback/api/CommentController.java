package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.domain.dto.CommentDto;
import ru.leyman.loco.locoback.service.CommentService;

import java.util.List;

@Slf4j
@Tag(name = "Комментарии", description = "Раздел управления комментариями")
@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @Operation(description = "Добавление комментария к посту")
    @PostMapping
    public CommentDto create(@RequestBody CommentDto commentDto) {
        log.info("Received create by commentDto={}", commentDto);
        return service.create(commentDto);
    }

    @Operation(description = "Получение всех комментариев к посту")
    @GetMapping
    public List<CommentDto> getByPost(@RequestParam Long postId) {
        log.info("Received getByPost={}", postId);
        return service.getByPost(postId);
    }

}
