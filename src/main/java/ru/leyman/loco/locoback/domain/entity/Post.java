package ru.leyman.loco.locoback.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.leyman.loco.locoback.domain.enums.PostCategory;
import ru.leyman.loco.locoback.domain.enums.PostState;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post {

    public Post(User author) {
        this.author = author;
        this.state = PostState.CREATED;
        this.created = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User author;
    private PostState state;
    private LocalDateTime created;
    private String text;
    private PostCategory category;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
