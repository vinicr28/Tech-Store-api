package com.ericsson.rampup.resources;

import com.ericsson.rampup.dto.UserDTO;
import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.resources.view.View;
import com.ericsson.rampup.services.UserService;
import com.ericsson.rampup.services.exceptions.EmailAlreadyRegisteredExeption;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    private static final String PATH_ID = "/{id}";

    @Autowired
    private UserService service;

    @GetMapping(value = "/page/{page}")
    @JsonView(View.Base.class)
    @PreAuthorize("hasRole('0')")
    public ResponseEntity<List<UserDTO>> findAll(@PathVariable int page){
        List<User> list = service.findAll(page);
        List<UserDTO> listDto = list.stream().map(user -> new UserDTO(user)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = PATH_ID)
    @JsonView(View.Base.class)
    @PreAuthorize("hasRole('0') || authentication.principal == @userRepository.findById(#id).get().getEmail()")
    public ResponseEntity<User> findById (@PathVariable Long id) {
        User obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/login/{email}")
    public ResponseEntity<UserDTO> findByEmail (@PathVariable String email) {
        UserDTO obj = service.findByEmail(email);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<User> insert(@Valid @RequestBody UserDTO objDTO){
       try {
           User obj = service.insert(objDTO);
           URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(PATH_ID).buildAndExpand(obj.getId()).toUri();
           return ResponseEntity.created(uri).body(obj);
       } catch (IdNotFoundExeption e){
           return ResponseEntity.notFound().build();
       }
    }

    @PostMapping
    @PreAuthorize("hasRole('0')")
    public ResponseEntity<Void> insertAdm(@Valid @RequestBody UserDTO objDTO){
        try {
            User obj = service.insertAdm(objDTO);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(PATH_ID).buildAndExpand(obj.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (IdNotFoundExeption e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = PATH_ID)
    @PreAuthorize("hasRole('0') || authentication.principal == @userRepository.findById(#id).get().getEmail()")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = PATH_ID)
    @PreAuthorize("hasRole('0') || authentication.principal == @userRepository.findById(#id).get().getEmail()")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserDTO objDto){
        User obj = service.fromDTO(objDto);
        obj = service.update(id,obj);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/total")
    @PreAuthorize("hasRole('0')")
    @JsonView(View.Base.class)
    public ResponseEntity<Integer> totalOfUsers(){
        Integer count = service.totalOfUsers();
        return ResponseEntity.ok().body(count);
    }
}
