package ru.leyman.loco.locoback.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.leyman.loco.locoback.domain.dto.UserDto;
import ru.leyman.loco.locoback.service.UserService;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public UserDto get() {
        return service.get();
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto userDto) {
        return service.update(userDto);
    }

}
