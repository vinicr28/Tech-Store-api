package com.ericsson.rampup.resources;

import com.ericsson.rampup.entities.Role;
import com.ericsson.rampup.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/roles")
public class RoleResource {

    private static final String PATH_ID = "/{id}";

    @Autowired
    private RoleService service;

    @GetMapping(value = "/page/{page}")
    public ResponseEntity<List<Role>> findAll(@PathVariable int page){
        List<Role> list = service.findAll(page);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = PATH_ID)
    public ResponseEntity<Role> findById (@PathVariable Long id) {
        Role obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }
}
