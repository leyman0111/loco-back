package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.domain.dto.ReactionDto;
import ru.leyman.loco.locoback.service.ReactionService;

import java.util.List;

@Slf4j
@Tag(name = "Реакции", description = "Раздел управления реакциями")
@RestController
@RequestMapping("reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService service;

    @Operation(description = "Создание реакции")
    @PostMapping
    public ReactionDto upsert(@RequestBody ReactionDto reactionDto) {
        log.info("Received upsert by reactionDto={}", reactionDto);
        return service.upsert(reactionDto);
    }

    @Operation(description = "Получить все рекции к посту")
    @GetMapping
    public List<ReactionDto> getByPost(@RequestParam Long postId) {
        log.info("Received getByPost={}", postId);
        return service.getByPost(postId);
    }

    @Operation(description = "Удалить реакцию")
    @DeleteMapping
    public void delete(@RequestParam Long id) {
        log.info("Received delete by id={}", id);
        service.delete(id);
    }

}
