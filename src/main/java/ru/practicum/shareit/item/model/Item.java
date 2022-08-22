package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * класс сдаваемой в аренду вещи
 */
@Data
@Builder
@Entity
@Table(name = "items", schema = "public")
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private Boolean available;
    @ManyToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    public Item() {

    }
}
