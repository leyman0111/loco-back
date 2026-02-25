package ru.leyman.loco.locoback.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private User author;

    @ManyToOne(optional = false)
    private Post post;

    private String text;

    @OneToMany(mappedBy = "comment")
    private List<Reaction> reactions;

    private LocalDateTime created;

}
