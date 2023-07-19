package com.ericsson.rampup.services;

import com.ericsson.rampup.dto.AddressDTO;
import com.ericsson.rampup.entities.Address;
import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.repositories.AddressRepository;
import com.ericsson.rampup.repositories.CustomerRepository;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import com.ericsson.rampup.services.exceptions.NotFoundExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    private static final String INVALID_STREET_MESSAGE = "Must insert a valid street!";
    private static final String INVALID_HOUSE_NUMBER_MESSAGE = "Must insert a valid house number!";
    private static final String INVALID_NEIGHBORHOOD_MESSAGE = "Must insert a valid neighborhood!";
    private static final String INVALID_COUNTRY_MESSAGE = "Must insert a valid country!";


    public List<Address> findAll(int page){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable =  PageRequest.of(page, 10, sort);
        return repository.findAllByDeleted(pageable, false).stream().toList();
    }

    public Address findById(Long id){
        Optional<Address> obj = repository.findById(id);
        return obj.orElseThrow(() -> new IdNotFoundExeption(id));
    }

    public Address insert(AddressDTO objDto){
        Optional<Customer> customerOpt = customerRepository.findById(objDto.getCustomerId());

        if(customerOpt.isEmpty()){
            throw new NotFoundExeption("Customer not found!");
        }

        Customer customer = customerOpt.get();
        Address obj = fromDTO(objDto);

        validateFields(obj);

        customer.addAddress(obj);
        customerRepository.save(customer);

        obj.setCustomer(customer);
        return repository.save(obj);
    }

    public void delete(Long id){
        findById(id);
        repository.deleteById(id);
    }

    public Address update(Long customerId,Long id, Address obj){
        try{

            Optional<Customer> customer = customerRepository.findById(customerId);
            if (customer.isEmpty()){
                throw new IdNotFoundExeption(customerId);
            }

            if(!customer.get().getAddresses().stream().anyMatch(x -> x.getId() == id)){
              throw new NotFoundExeption("No address found");
            }

            Optional<Address> entity = repository.findById(id);

            updateData(entity.get(), obj);
            return repository.save(entity.get());
        } catch (EntityNotFoundException | NullPointerException e){
            throw new IdNotFoundExeption(id);
        }

    }

    public Address fromDTO(AddressDTO objDTO){
        return new Address(objDTO.getStreet(), objDTO.getHouseNumber(), objDTO.getNeighborhood(),
                objDTO.getZipCode(), objDTO.getCountry(), objDTO.getAddressType());
    }

    private void updateData(Address entity, Address obj) {
        if (obj.getStreet() != null){
            entity.setStreet(obj.getStreet());
        }
        if (obj.getHouseNumber() != null){
            entity.setHouseNumber(obj.getHouseNumber());
        }
        if (obj.getNeighborhood() != null){
            entity.setNeighborhood(obj.getNeighborhood());
        }
        if (obj.getZipCode() != null){
            entity.setZipCode(obj.getZipCode());
        }
        if (obj.getCountry() != null){
            entity.setCountry(obj.getCountry());
        }
        if (obj.getAddressType() != null){
            entity.setAddressType(obj.getAddressType());
        }
    }

    public void validateFields (Address address) {
        if(address.getStreet().isBlank()){
            throw new IllegalArgumentException(INVALID_STREET_MESSAGE);
        }
        if(address.getHouseNumber() < 1){
            throw new IllegalArgumentException(INVALID_HOUSE_NUMBER_MESSAGE);
        }
        if(address.getNeighborhood().isBlank()){
            throw new IllegalArgumentException(INVALID_NEIGHBORHOOD_MESSAGE);
        }
        if(address.getCountry().isBlank()){
            throw new IllegalArgumentException(INVALID_COUNTRY_MESSAGE);
        }
    }

}
