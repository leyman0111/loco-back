package ru.leyman.loco.locoback.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.leyman.loco.locoback.domain.dto.ReactionDto;
import ru.leyman.loco.locoback.service.ReactionService;

@RestController
@RequestMapping("reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService service;

    @PostMapping
    public ReactionDto create(@RequestBody ReactionDto reactionDto) {
        return service.create(reactionDto);
    }
}
