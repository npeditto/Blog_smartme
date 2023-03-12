package com.projects.blog.controllers.response;

import com.projects.blog.resources.UserDTO;
import lombok.Data;

@Data
public class AuthorPatchResponse {
    private UserDTO author;
}
