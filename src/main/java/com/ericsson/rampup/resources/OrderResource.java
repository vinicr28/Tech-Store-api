package com.ericsson.rampup.resources;

import com.ericsson.rampup.dto.OrderDTO;
import com.ericsson.rampup.dto.ReportOrderWeekDTO;
import com.ericsson.rampup.entities.Order;
import com.ericsson.rampup.resources.view.View;
import com.ericsson.rampup.services.OrderService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

    private static final String PATH_ID = "/{id}";

    @Autowired
    private OrderService service;

    @GetMapping(value = "/page/{page}")
    @PreAuthorize("hasRole('0')")
    @JsonView(View.Base.class)
    public ResponseEntity<List<Order>> findAll(@PathVariable int page){
        List<Order> list = service.findAll(page);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/user_id/{id}")
    @PreAuthorize("hasRole('0') || authentication.principal == @userRepository.findById(#id).get().getEmail()")
    @JsonView(View.Base.class)
    public ResponseEntity<List<Order>> findAllFilter(@PathVariable Long id){
        List<Order> list = service.findAllFilter(id);
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = PATH_ID)
    @JsonView(View.Base.class)
    @PreAuthorize("hasRole('0') || authentication.principal == @orderRepository.findById(#id).get().getCustomer().getUser().getEmail()")
    public ResponseEntity<Order> findById (@PathVariable Long id) {
        Order obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody OrderDTO objDto){
        Order obj = service.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(PATH_ID).buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = PATH_ID)
    @PreAuthorize("hasRole('0') || authentication.principal == @orderRepository.findById(#id).get().getCustomer().getUser().getEmail()")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = PATH_ID)
    @PreAuthorize("hasRole('0')")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order obj){
        obj = service.update(id,obj);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/count")
    @PreAuthorize("hasRole('0')")
    @JsonView(View.Base.class)
    public ResponseEntity<Integer> countOrdersToday(){
        Integer count = service.countToday();
        return ResponseEntity.ok().body(count);
    }

    @GetMapping(value = "/invoicing")
    @PreAuthorize("hasRole('0')")
    @JsonView(View.Base.class)
    public ResponseEntity<Double> invoicing(){
        Double count = service.invoicing();
        return ResponseEntity.ok().body(count);
    }

    @GetMapping(value = "/report")
    @PreAuthorize("hasRole('0')")
    @JsonView(View.Base.class)
    public ResponseEntity<List<ReportOrderWeekDTO>> reportOrderPerWeek(){
        List<ReportOrderWeekDTO> list = service.orderChar();
        return ResponseEntity.ok().body(list);
    }

}
