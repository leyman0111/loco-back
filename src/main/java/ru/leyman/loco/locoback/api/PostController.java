package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.domain.dto.*;
import ru.leyman.loco.locoback.service.PostService;

import java.util.List;

@Tag(name = "Посты", description = "Раздел управления постами")
@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @Operation(description = "Получение ближайших постов")
    @PostMapping("scope")
    public List<PostMark> getByScope(@RequestBody Scope scope) {
        return service.getByScope(scope);
    }

    @Operation(description = "Получение поста для отображения внизу карты")
    @GetMapping("previews/{id}")
    public PostPreview getPreview(@PathVariable Long id) {
        return service.getPreview(id);
    }

    @Operation(description = "Получение поста с комментариями")
    @GetMapping("details/{id}")
    public PostDetails getDetails(@PathVariable Long id) {
        return service.getDetails(id);
    }

    @Operation(description = "Получение черновика поста, а при отсутствии - создание")
    @PostMapping
    public PostDto create() {
        return service.create();
    }

    @Operation(description = "Публикация поста")
    @PutMapping
    public PostDto update(@RequestBody PostDto postDto) {
        return service.update(postDto);
    }

}
