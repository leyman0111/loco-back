package ru.leyman.loco.locoback.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.leyman.loco.locoback.domain.dto.PostDetails;
import ru.leyman.loco.locoback.domain.dto.PostDto;
import ru.leyman.loco.locoback.domain.dto.PostMark;
import ru.leyman.loco.locoback.domain.dto.PostPreview;
import ru.leyman.loco.locoback.domain.entity.Comment;
import ru.leyman.loco.locoback.domain.entity.Content;
import ru.leyman.loco.locoback.domain.entity.Post;
import ru.leyman.loco.locoback.domain.entity.Reaction;

import java.util.List;

@Mapper(uses = {ReactionMapper.class,
        CommentMapper.class, UserMapper.class})
public interface PostMapper {

    PostDto map(Post post);

    PostDto map(Post post, List<Content> contents);

    PostMark mapToMark(Post post);

    PostPreview mapToPreview(Post post, List<Content> contents,
                             List<Reaction> reactions);

    PostDetails mapToDetails(Post post, List<Content> contents,
                             List<Reaction> reactions, List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "created", ignore = true)
    void map(@MappingTarget Post post, PostDto postDto);

    default Long mapContent(Content content) {
        return content.getId();
    }

}
