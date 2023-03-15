package com.projects.blog.utils.mappers;

import com.projects.blog.models.User;
import com.projects.blog.resources.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper extends ModelMapperUtil {

    public UserMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    public UserDTO toUserDTO(User user){
        return this.map(user, UserDTO.class);
    }

    public List<UserDTO> toUserDTOList(List<User> user){
        return this.mapList(user, UserDTO.class);
    }

}
