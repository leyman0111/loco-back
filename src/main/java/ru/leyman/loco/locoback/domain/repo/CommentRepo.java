package ru.leyman.loco.locoback.domain.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.leyman.loco.locoback.domain.entity.Comment;
import ru.leyman.loco.locoback.domain.entity.Post;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"reactions"})
    List<Comment> findAllByPost(Post post);

}
