package ru.leyman.loco.locoback.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.leyman.loco.locoback.domain.enums.ReactionType;

@Entity
@Getter @Setter
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User author;

    private ReactionType type;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Comment comment;

}
