package com.projects.blog.controllers;

import com.projects.blog.controllers.request.post.PostPostRequest;
import com.projects.blog.exceptionHandlers.exception.resource.ResourceNotFound;
import com.projects.blog.models.Post;
import com.projects.blog.models.User;
import com.projects.blog.resources.PostDTO;
import com.projects.blog.resources.hateoas.PostDTOAssembler;
import com.projects.blog.services.interfaces.IPostService;
import com.projects.blog.utils.mappers.PostMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Controller utilizzato per interagire con l'utente mediante chiamate HTTP.
 * Sarà lui ad utilizzare i servizi esposti dalla nostra applicazione Java al
 * fine di eseguire determinati compiti. Anche in questo caso viene utilizzato
 * lombok (@RequiredArgsConstructor) il quale permetterà di effettuare l'autowiring
 * con i servizi utilizzati. Inoltre, viene immesso un "prefix" /post per
 * interagire con la risorsa.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    /**
     * Nei servizi sottostanti è stata utilizzata un'interfaccia per aumentare il
     * livello di Loose Coupling delle risorse, in questo modo sarà sempre possibile
     * attraverso un @Qualifier fare il cambio (binding) del servizio di Post interessato.
     */
    private final IPostService postService;

    /**
     * Oggetto di tipo Mapper che permetterà di copiare i valori di un record del database e
     * trasformarla in una risorsa restituibile (post) andando però a celare alcuni
     * campi come il post_id.
     */

    private final PostMapper mapperService;

    /**
     * Oggetto impiegato principalmente per la costruzione dell'HATEOAS
     * e permette di creare tutti i link per mantenere
     * lo stato dell'applicativo (HATEOAS) in maniera dinamica.
     */
    private final PostDTOAssembler postDTOAssembler;

    /**
     * Metodo GET nella quale si ottengono tutti i post presenti nel database
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PostDTO>>> getPosts(){
        // Dal servizio Post prelevo tutti i post
        List<Post> posts = postService.getPosts();

        // Converto tutti gli oggetti Post (record del DB) in risorse (rappresentazione del record e non il record stesso)
        List<PostDTO> postDTOS = mapperService.toPostDTOList(posts);

        // Creo, essendo una collezione, una collezione di modelli in cui ad ognuno di essi vengono aggiunti i link dell'HATEOAS (sia ai singoli modelli che alla collezione)
        return ResponseEntity.ok(postDTOAssembler.toCollectionModel(postDTOS));
    }


    /**
     * Metodo GET per un solo post presente nel database
     * @param id ID Post che si vuole consultare
     * @return Risposta con l'entità PostDTO (rappresentazione del Post)
     * @throws ResourceNotFound - Eccezione se non viene trovata la risorsa
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PostDTO>> getPostByID(@PathVariable long id) throws ResourceNotFound {
        Post p = postService.getPost(id);

        PostDTO post = mapperService.toPostDTO(p);
        return ResponseEntity.ok(postDTOAssembler.toModel(post));
    }

    /**
     * Metodo che crea un post che viene associato all'utente di cui si passa il token. In particolare
     * viene preso il contesto di sicurezza associato al token (ricorda JWTAuthFilter). Dopo di che
     * vien creato e salvato un post nella quale si impostano il contenuto e l'autore. Una volta fatto
     * viene convertito l'oggetto in risorsa (oggetto DTO) che verrà poi restituita al client (con
     * collegamento HATEOAS).
     *
     */
    @PostMapping
    public ResponseEntity<EntityModel<PostDTO>> createPost(@RequestBody @Valid PostPostRequest postRequest){
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = Post.builder().contenuto(postRequest.getContenuto())
                                  .autore(user)
                                  .build();

        postService.save(post);

        // Copio i valori del post all'interno di un oggetto PostDTO
        PostDTO postDTO = mapperService.toPostDTO(post);

        // Collegamento HATEOAS e ritorno la risposta
        return ResponseEntity.ok(postDTOAssembler.toModel(postDTO));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable long id) throws ResourceNotFound {
        HashMap<String, String> res = new HashMap<>();
        res.put("ok", "Risorsa eliminata");

        // Cancellazione della risorsa e restituzione risposta {"ok" : "risorsa eliminata"}
        postService.delete(id);
        return res;
    }

    @PutMapping("/{id}")
    public Map<String, String> update(@PathVariable long id, @RequestBody @Valid PostPostRequest post) throws ResourceNotFound {
        HashMap<String, String> res = new HashMap<>();
        res.put("ok", "Risorsa modificata");

        // Modifica della risorsa con id :id e restituzione risposta {"ok" : "risorsa modificata"}
        postService.update(id, post.getContenuto());
        return res;
    }
}
