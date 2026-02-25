package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.leyman.loco.locoback.domain.dto.UserDto;
import ru.leyman.loco.locoback.domain.entity.User;
import ru.leyman.loco.locoback.domain.repo.UserRepo;
import ru.leyman.loco.locoback.mapper.UserMapper;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserDto get() {
        return userMapper.map(this.getCurrentUser());
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByEmail(authentication.getName())
                .orElse(null);
    }

    public UserDto update(UserDto userDto) {
        var user = userRepo.getReferenceById(userDto.id());
        if (!this.getCurrentUser().equals(user)) {
            throw new RuntimeException();
        }
        userMapper.map(user, userDto);
        return userMapper.map(userRepo.save(user));
    }

    public Optional<User> findByEmail(String email) {
        return StringUtils.hasText(email) ? userRepo.findByEmail(email) : Optional.empty();
    }

    public User save(User user) {
        return userRepo.save(user);
    }

}
