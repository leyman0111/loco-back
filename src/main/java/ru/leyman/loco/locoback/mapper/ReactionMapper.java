package ru.leyman.loco.locoback.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.leyman.loco.locoback.domain.dto.ReactionDto;
import ru.leyman.loco.locoback.domain.entity.Reaction;

@Mapper(uses = UserMapper.class)
public interface ReactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    Reaction map(ReactionDto reactionDto);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "commentId", source = "comment.id")
    ReactionDto map(Reaction reaction);

}
