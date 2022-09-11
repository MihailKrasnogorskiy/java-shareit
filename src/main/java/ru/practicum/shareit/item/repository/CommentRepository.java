package ru.practicum.shareit.item.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * репозиторий комментариев
 */
public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByItemId(long itemId);
}
