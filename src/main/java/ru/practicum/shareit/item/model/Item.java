package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @Column(name = "owner_id")
    private long owner;
    private long requestId;

    public Item() {

    }
}
