package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * класс пользователя
 */
@Builder
@Data
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User() {

    }
}
