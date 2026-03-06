package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.leyman.loco.locoback.domain.dto.ReactionDto;
import ru.leyman.loco.locoback.service.ReactionService;

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

}
