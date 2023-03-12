package com.projects.blog.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.blog.models.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class UserDTO extends RepresentationModel<UserDTO> {
    @JsonIgnore
    private long user_id;

    private String email;

    private String nome;

    private String cognome;

    private LocalDate data_nascita;

    @JsonIgnore
    private List<Post> posts;

    @JsonIgnore
    public long getUserID() {
        return user_id;
    }
}
