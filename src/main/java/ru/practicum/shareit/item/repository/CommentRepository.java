package ru.practicum.shareit.item.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

public List<Comment> findByItemId(long itemId);
}
