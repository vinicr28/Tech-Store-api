package com.ericsson.rampup.resources;

import com.ericsson.rampup.dto.RequestDTO;
import com.ericsson.rampup.entities.Request;
import com.ericsson.rampup.resources.view.View;
import com.ericsson.rampup.services.RequestService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/requests")
public class RequestResource {

    private static final String REQUEST_ID = "/request_id/{id}";

    @Autowired
    private RequestService service;

    @GetMapping(value = "/page/{page}")
    @JsonView(View.Base.class)
    public ResponseEntity<List<Request>> findAll(@PathVariable int page){
        List<Request> list = service.findAll(page);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = REQUEST_ID)
    public ResponseEntity<Request> findById(@PathVariable Long id){
        Request request = service.findById(id);
        return ResponseEntity.ok().body(request);
    }

    @DeleteMapping(value = REQUEST_ID)
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.Delete(id);
        return ResponseEntity.ok().build();
    }


}
