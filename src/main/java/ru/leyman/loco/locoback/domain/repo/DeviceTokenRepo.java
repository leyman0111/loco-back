package ru.leyman.loco.locoback.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leyman.loco.locoback.domain.entity.DeviceToken;
import ru.leyman.loco.locoback.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepo extends JpaRepository<DeviceToken, Long> {

    List<DeviceToken> findAllByUser(User user);

    Optional<DeviceToken> findByToken(String token);
}