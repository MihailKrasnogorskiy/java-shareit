package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

/**
 * маппер комментариев
 */
@Component
public class CommentMapper {

    /**
     * создание dto объекта коментария
     *
     * @param comment объект комментария
     * @return dto объекта коментария
     */
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    /**
     * создание объекта комментария
     *
     * @param commentDto dto объекта коментария
     * @return объект комментария
     */
    public Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .build();
    }
}
