package ru.leyman.loco.locoback.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.leyman.loco.locoback.domain.entity.Post;
import ru.leyman.loco.locoback.domain.entity.Reaction;

import java.util.List;

@Repository
public interface ReactionRepo extends JpaRepository<Reaction, Long> {

    List<Reaction> findAllByPost(Post post);

}
