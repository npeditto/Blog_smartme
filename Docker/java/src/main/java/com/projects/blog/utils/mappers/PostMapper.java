package com.projects.blog.utils.mappers;

import com.projects.blog.models.Post;
import com.projects.blog.resources.PostDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper extends ModelMapperUtil {

    public PostMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    /**
     * @param post Post da convertire in PostDTO (rappresentazione della risorsa Post)
     */
    public PostDTO toPostDTO(Post post){
        return this.map(post, PostDTO.class);
    }

    public List<PostDTO> toPostDTOList(List<Post> posts){
        return this.mapList(posts, PostDTO.class);
    }
}
