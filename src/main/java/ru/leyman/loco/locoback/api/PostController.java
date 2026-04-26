package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.domain.dto.*;
import ru.leyman.loco.locoback.service.PostService;

import java.util.List;

@Slf4j
@Tag(name = "Посты", description = "Раздел управления постами")
@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @Operation(description = "Получение ближайших постов")
    @PostMapping("scope")
    public List<PostMark> getByScope(@RequestBody Scope scope) {
        log.info("Received getByScope by scope={}", scope);
        return service.getByScope(scope);
    }

    @Operation(description = "Получение поста для отображения внизу карты")
    @GetMapping("previews")
    public List<PostPreview> getPreview(@RequestParam List<Long> ids) {
        log.info("Received getPreview by id={}", ids);
        return service.getPreview(ids);
    }

    @Operation(description = "Получение поста с комментариями")
    @GetMapping("details/{id}")
    public PostDetails getDetails(@PathVariable Long id) {
        log.info("Received getDetails by id={}", id);
        return service.getDetails(id);
    }

    @Operation(description = "Получение черновика поста, а при отсутствии - создание")
    @PostMapping
    public PostDto create() {
        log.info("Received create");
        return service.create();
    }

    @Operation(description = "Публикация поста")
    @PutMapping
    public PostDto update(@RequestBody PostDto postDto) {
        log.info("Received update by postDto={}", postDto);
        return service.update(postDto);
    }

    @Operation(description = "Получение своих постов")
    @GetMapping
    public List<PostDto> getAllMine(@RequestParam(required = false) Boolean draft) {
        log.info("Received getAllMine");
        return service.getAllMine(draft);
    }

    @Operation(description = "Удаление черновика поста")
    @DeleteMapping
    public void delete() {
        log.info("Received delete");
        service.delete();
    }

}
