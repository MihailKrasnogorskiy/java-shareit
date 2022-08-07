package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "requests", schema = "public")
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;
    private LocalDateTime created;

    public ItemRequest() {

    }
}
