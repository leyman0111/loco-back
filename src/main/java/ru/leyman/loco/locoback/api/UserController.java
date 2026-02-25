package ru.leyman.loco.locoback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.domain.dto.UserDto;
import ru.leyman.loco.locoback.service.UserService;

@Tag(name = "Пользователи", description = "Раздел управления пользователями")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(description = "Получение пользователя для отображения в разделе настроек")
    @GetMapping
    public UserDto get() {
        return service.get();
    }

    @Operation(description = "Обновление настроек пользователя")
    @PutMapping
    public UserDto update(@RequestBody UserDto userDto) {
        return service.update(userDto);
    }

}
