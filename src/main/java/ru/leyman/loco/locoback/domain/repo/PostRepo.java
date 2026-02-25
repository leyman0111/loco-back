package ru.leyman.loco.locoback.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.leyman.loco.locoback.domain.entity.Post;
import ru.leyman.loco.locoback.domain.enums.PostCategory;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    @Query(nativeQuery = true,
            value = "SELECT p.id, p.latitude, p.longitude, (6371 * acos(" +
                    "cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
                    "cos(radians(p.longitude) - radians(:longitude)) + " +
                    "sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance " +
                    "FROM post p WHERE p.latitude BETWEEN (:latitude - (:maxDistance / 111.0)) AND (:latitude + (:maxDistance / 111.0)) AND " +
                    "p.longitude BETWEEN (:longitude - (:maxDistance / (111.0 * COS(RADIANS(:latitude))))) AND (:longitude + (:maxDistance / (111.0 * COS(RADIANS(:latitude))))) " +
                    "HAVING distance <= :maxDistance ORDER BY distance")
    List<Post> getPostByLocation(BigDecimal latitude, BigDecimal longitude,
                                 Integer maxDistance, List<PostCategory> categories);

}
