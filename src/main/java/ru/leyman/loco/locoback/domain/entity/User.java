package ru.leyman.loco.locoback.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.leyman.loco.locoback.domain.enums.Locale;

@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;

    private String username;

    private String name;

    private String email;

    private Locale locale;

    private byte[] avatar;

    public User(String externalId, String username, String name, String email, Locale locale) {
        this.externalId = externalId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.locale = locale;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof User
                && ((User) object).getId().equals(id);
    }

}
