package com.ericsson.rampup.resources;

import com.ericsson.rampup.entities.ProductOffering;
import com.ericsson.rampup.resources.view.View;
import com.ericsson.rampup.services.ProductOfferingService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/productOfferings")
public class ProductOfferingResource {

    private static final String PATH_ID = "/{id}";

    @Autowired
    private ProductOfferingService service;

    @GetMapping(value = "/page/{page}")
    public ResponseEntity<List<ProductOffering>> findAll(@PathVariable int page){
        List<ProductOffering> list = service.findAll(page);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = PATH_ID)
    public ResponseEntity<ProductOffering> findById (@PathVariable Long id) {
        ProductOffering obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @PreAuthorize("hasRole('0')")
    public ResponseEntity<Void> insert(@Valid @RequestBody ProductOffering obj){
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(PATH_ID).buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = PATH_ID)
    @PreAuthorize("hasRole('0')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = PATH_ID)
    @PreAuthorize("hasRole('0')")
    public ResponseEntity<ProductOffering> update(@PathVariable Long id, @RequestBody ProductOffering obj){
        obj = service.update(id,obj);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/total")
    @PreAuthorize("hasRole('0')")
    @JsonView(View.Base.class)
    public ResponseEntity<Integer> totalOfProducts(){
        Integer count = service.totalOfProducts();
        return ResponseEntity.ok().body(count);
    }
}
