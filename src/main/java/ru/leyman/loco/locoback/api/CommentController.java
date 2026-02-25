package ru.leyman.loco.locoback.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.leyman.loco.locoback.domain.dto.CommentDto;
import ru.leyman.loco.locoback.service.CommentService;

@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @PostMapping
    public CommentDto create(@RequestBody CommentDto commentDto) {
        return service.create(commentDto);
    }

}
