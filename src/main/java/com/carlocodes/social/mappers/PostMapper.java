package com.carlocodes.social.mappers;

import com.carlocodes.social.dtos.PostDto;
import com.carlocodes.social.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "user.id", target = "userId")
    PostDto mapToDto(Post post);
    List<PostDto> mapToDtos(List<Post> posts);
}
