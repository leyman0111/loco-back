package ru.leyman.loco.locoback.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.leyman.loco.locoback.domain.enums.ContentType;

@Entity
@Getter
@NoArgsConstructor
public class Content {

    public Content(Post post, ContentType type, String origin) {
        this.post = post;
        this.type = type;
        this.origin = origin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    private ContentType type;

    private String origin;

    private String small;

    private String medium;

    private String large;

}
