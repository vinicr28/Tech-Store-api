package com.ericsson.rampup.resources;

import com.ericsson.rampup.entities.Address;
import com.ericsson.rampup.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/addresses")
public class AddressResource {

    private static final String PATH_ID = "/{id}";

    @Autowired
    private AddressService service;

    @GetMapping(value = "/page/{page}")
    public ResponseEntity<List<Address>> findAll(@PathVariable int page){
        List<Address> list = service.findAll(page);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = PATH_ID)
    public ResponseEntity<Address> findById (@PathVariable Long id) {
        Address obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }
}
