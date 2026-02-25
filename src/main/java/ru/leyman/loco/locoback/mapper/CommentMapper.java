package ru.leyman.loco.locoback.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.leyman.loco.locoback.domain.dto.CommentDto;
import ru.leyman.loco.locoback.domain.entity.Comment;

@Mapper(uses = {UserMapper.class, ReactionMapper.class})
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "reactions", ignore = true)
    Comment map(CommentDto commentDto);

    @Mapping(target = "postId", source = "post.id")
    CommentDto map(Comment comment);

}
