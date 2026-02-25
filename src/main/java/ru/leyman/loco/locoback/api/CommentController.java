package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.leyman.loco.locoback.domain.dto.CommentDto;
import ru.leyman.loco.locoback.service.CommentService;

@Tag(name = "Комментарии", description = "Раздел управления комментариями")
@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @Operation(description = "Добавление комментария к посту")
    @PostMapping
    public CommentDto create(@RequestBody CommentDto commentDto) {
        return service.create(commentDto);
    }

}
