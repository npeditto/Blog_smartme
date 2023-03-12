package com.projects.blog.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PostDTO {
    @JsonIgnore
    private UserDTO autore;

    private long post_id;
    private String contenuto;
}
