package ru.leyman.loco.locoback.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.leyman.loco.locoback.domain.entity.Content;
import ru.leyman.loco.locoback.domain.entity.Post;

import java.util.List;

@Repository
public interface ContentRepo extends JpaRepository<Content, Long> {

    List<Content> findAllByPost(Post post);

}
