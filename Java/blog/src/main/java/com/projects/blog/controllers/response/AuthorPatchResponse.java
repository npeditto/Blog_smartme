package com.projects.blog.controllers.response;

import com.projects.blog.resources.UserDTO;
import lombok.Builder;
import lombok.Data;

/**
 * Oggetto impiegato per la restituzione di una risposta subito dopo la
 * chiamata con il metodo patch all'URI /post/:id/author
 */
@Data
@Builder
public class AuthorPatchResponse {
    //
    private UserDTO author;
}
