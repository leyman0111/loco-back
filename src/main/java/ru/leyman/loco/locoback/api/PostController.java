package ru.leyman.loco.locoback.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.domain.dto.*;
import ru.leyman.loco.locoback.service.PostService;

import java.util.List;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @PostMapping("scope")
    public List<PostMark> getByScope(@RequestBody Scope scope) {
        return service.getByScope(scope);
    }

    @GetMapping("previews/{id}")
    public PostPreview getPreview(@PathVariable Long id) {
        return service.getPreview(id);
    }

    @GetMapping("details/{id}")
    public PostDetails getDetails(@PathVariable Long id) {
        return service.getDetails(id);
    }

    @PostMapping
    public PostDto create() {
        return service.create();
    }

    @PutMapping
    public PostDto update(@RequestBody PostDto postDto) {
        return service.update(postDto);
    }

}
