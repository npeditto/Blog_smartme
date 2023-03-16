package com.projects.blog.controllers;

import com.projects.blog.controllers.request.author.AuthorPutRequest;
import com.projects.blog.controllers.response.AuthorPatchResponse;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.Post;
import com.projects.blog.models.User;
import com.projects.blog.resources.UserDTO;
import com.projects.blog.resources.hateoas.AuthorPostDTOAssembler;
import com.projects.blog.services.interfaces.IPostService;
import com.projects.blog.services.interfaces.IUserService;
import com.projects.blog.utils.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller utilizzato per interagire con l'utente mediante chiamate HTTP.
 * Sarà lui ad utilizzare i servizi esposti dalla nostra applicazione Java al
 * fine di eseguire determinati compiti. Anche in questo caso viene utilizzato
 * lombok (@RequiredArgsConstructor) il quale permetterà di effettuare l'autowiring
 * con i servizi utilizzati. Inoltre, viene immesso un "prefix" /post/{id}/author per
 * interagire con la risorsa, in questo caso la risorsa è identificata dinamicamente
 * grazie alla presenza {id}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post/{id}/author")
public class AuthorPostController
{

    /**
     * Nei servizi sottostanti è stata utilizzata un'interfaccia per aumentare il
     * livello di Loose Coupling delle risorse, in questo modo sarà sempre possibile
     * attraverso un @Qualifier fare il cambio (binding) del servizio di Post interessato.
     */

    // Servizi esposti per interagire con i post
    private final IPostService postService;

    // Servizi esposti per interagire con gli utenti.
    private final IUserService userService;

    /**
     * Oggetto di tipo Mapper che permetterà di copiare i valori di un record del database e trasformarla in una risorsa
     * restituibile dall'utente (DTO) andando però a celare alcuni campi come le password.
     */
    private final UserMapper userMapper;

    /**
     * Oggetto impiegato principalmente per la costruzione dell'HATEOAS, questo in particolare
     * viene ereditato da UserDTOAssembler e permette di creare tutti i link per mantenere
     * lo stato dell'applicativo (HATEOAS) in maniera dinamica.
     */
    private final AuthorPostDTOAssembler authorPostDTOAssembler;

    // Mappatura del metodo get sulla risorsa /post/:id/author a cui viene passata una variabile di path "id"
    @GetMapping
    public ResponseEntity<EntityModel<UserDTO>> getAuthor(@PathVariable long id) throws ResourceNotFound {
        // Ottieni l'oggetto post con id = :id, se non lo trovi lancia l'eccezione ResourceNotFound
        Post post = postService.getPost(id);

        // Trasforma l'autore del post (Conservato come oggetto User) in UserDTO.
        // L'UserDTO rappresenterà il record del database nascondendo però alcuni campi.
        UserDTO author = userMapper.toUserDTO(post.getAutore());

        authorPostDTOAssembler.setPost(post);

        // Ritorno l'entità come risposta (conversione a modello) a cui verranno aggiunti tutti i link per
        // i collegamenti alle altre risorse e alle altre operazioni.

        return ResponseEntity.ok(authorPostDTOAssembler.toModel(author));
    }

    // Mappatura del metodo Patch sulla risorsa /post/:id/author a cui viene passata una variabile di path "id" e, nel corpo della richiesta,
    // in formato JSON una serie di parametri come indicato dallo schema "AuthorPutRequest" (deve passare obbligatoriamente - fa parte della
    // validazione - il campo autore).
    @PatchMapping
    public AuthorPatchResponse modifyAuthor(@PathVariable long id, @RequestBody @Valid AuthorPutRequest authorRequest) throws ResourceNotFound {
        // Ottengo l'oggetto Post con id = :id, se non lo trovi lancia l'eccezione ResourceNotFound
        Post post = postService.getPost(id);

        // Cerca l'autore, se non lo trovi lancia l'eccezione ResourceNotFound
        User author = userService.getUser(authorRequest.getAutore());

        // Imposta l'autore nel post.
        post.setAutore(author);

        postService.save(post);

        return AuthorPatchResponse.builder()
                                  .author(userMapper.toUserDTO(author))
                                  .build();
    }
}
