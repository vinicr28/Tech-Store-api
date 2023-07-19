package com.ericsson.rampup.resources;

import com.ericsson.rampup.dto.AddressDTO;
import com.ericsson.rampup.dto.CustomerDTO;
import com.ericsson.rampup.dto.ViaCepDTO;
import com.ericsson.rampup.entities.Address;
import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.resources.view.View;
import com.ericsson.rampup.services.AddressService;
import com.ericsson.rampup.services.CustomerService;
import com.ericsson.rampup.services.ViaCepService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/customers")
public class CustomerResource {

    private static final String PATH_ID = "/{id}";

    @Autowired
    private CustomerService service;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ViaCepService viaCepService;

    @GetMapping(value = "/page/{page}")
    @JsonView(View.Base.class)
    @PreAuthorize("hasRole('0')")
    public ResponseEntity<List<CustomerDTO>> findAll(@PathVariable int page){
        List<Customer> list = service.findAll(page);
        List<CustomerDTO> listDto = list.stream().map(x -> new CustomerDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = PATH_ID)
    @JsonView(View.Base.class)
    @PreAuthorize("hasRole('0') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")
    public ResponseEntity<Customer> findById (@PathVariable Long id) {
        Customer obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @PreAuthorize("hasRole('0') || hasRole('1')")
    public ResponseEntity<Customer> insert(@Valid @RequestBody CustomerDTO objDto){
        Customer obj = service.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(PATH_ID).buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = PATH_ID)
    @PreAuthorize("hasRole('0') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = PATH_ID)
    @PreAuthorize("hasRole('0') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Customer obj){
        obj = service.update(id,obj);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = PATH_ID + "/addresses")
    @PreAuthorize("hasRole('0') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")
    public ResponseEntity<List<Address>> findAllAddresses(@PathVariable Long id){
        List<Address> addresses = service.findAllAddressesByCustomer(id);
        return ResponseEntity.ok().body(addresses);
    }

    @GetMapping(value = "/addresses/id/{id}")
    @PreAuthorize("hasRole('0') || authentication.principal == @addressRepository.findById(#id).get().getCustomer().getUser().getEmail()")
    public ResponseEntity<Address> findAddressById(@PathVariable Long id){
        Address address = service.findAddressById(id);
        return ResponseEntity.ok().body(address);
    }

    @PostMapping(value = "/addresses")
    @PreAuthorize("hasRole('0') || hasRole('1')")
    public ResponseEntity<Address> insertAddress(@RequestBody AddressDTO addressDTO){
        Address address = addressService.insert(addressDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/addresses/id/{id}").buildAndExpand(address.getId()).toUri();
        return ResponseEntity.created(uri).body(address);
    }

    @PatchMapping(value = PATH_ID + "/addresses/id/{addressId}")
    @PreAuthorize("hasRole('0') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")
    public  ResponseEntity<Address> updateAddress(@PathVariable Long id, @PathVariable Long addressId, @RequestBody Address obj){
        Address address = addressService.update(id, addressId, obj);
        return ResponseEntity.ok().body(address);
    }

    @DeleteMapping(value = "/addresses/id/{id}")
    @PreAuthorize("hasRole('0') || authentication.principal == @addressRepository.findById(#id).get().getCustomer().getUser().getEmail()")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id){
        addressService.delete(id);
        return  ResponseEntity.noContent().build();
    }

    //ViaCep---------------------------

    @PostMapping(value = "/viacep")
    @PreAuthorize("hasRole('0') || hasRole('1')")
    public ResponseEntity<Address> insertAddressByCep(@RequestBody ViaCepDTO viaCepDTO){
        try{
            Address obj = viaCepService.getViaCep(viaCepDTO.getCep(), viaCepDTO.getNumero(), viaCepDTO.getCustomerId());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/viacep").buildAndExpand(obj.getId()).toUri();
            return  ResponseEntity.created(uri).body(obj);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
