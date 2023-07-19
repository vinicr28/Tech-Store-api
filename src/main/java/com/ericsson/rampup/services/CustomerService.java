package com.ericsson.rampup.services;

import com.ericsson.rampup.dto.CustomerDTO;
import com.ericsson.rampup.entities.Address;
import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.repositories.AddressRepository;
import com.ericsson.rampup.repositories.CustomerRepository;
import com.ericsson.rampup.repositories.UserRepository;
import com.ericsson.rampup.services.exceptions.AlreadyHaveCustomerExeption;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import com.ericsson.rampup.services.exceptions.NotFoundExeption;
import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private static final String INVALID_NAME_MESSAGE = "Must insert a valid full name!";
    private static final String INVALID_DOCUMENT_MESSAGE = "Must insert a valid document number!";

    public List<Customer> findAll(int page){
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(page,10, sort);
        return repository.findAllByDeleted(pageable, false).stream().toList();
    }

    public Customer findById(Long id){
        Optional<Customer> obj = repository.findById(id);
        if (obj.get().getDeleted()){
            obj = Optional.empty();
        }
        return obj.orElseThrow(() -> new IdNotFoundExeption(id));
    }

    public Customer insert(CustomerDTO objDto){
        Optional<User> userOpt = userRepository.findById(objDto.getUserId());

        if(userOpt.isEmpty()){
            throw new NotFoundExeption("User not found!");
        }

        User user = userOpt.get();
        Customer obj = fromDTO(objDto);

        validateFields(obj);

        if(user.getCustomer() != null){
            if (!user.getCustomer().getDeleted()){
                throw new AlreadyHaveCustomerExeption(user.getCustomer().getId());
            }else {
                Customer customerUpdated = update(user.getCustomer().getId(), obj);
                repository.save(customerUpdated);
            }
        }else {
            obj.setPassword(user.getPassword());

            user.setCustomer(obj);
            obj.setUser(user);
            repository.save(obj);
            userRepository.save(user);
        }

        return obj;
    }

    public void delete(Long id){
        findById(id);
        repository.deleteById(id);
    }

    @Transactional
    public Customer update(Long id, Customer obj){
        try{
            Optional<Customer> entity = repository.findById(id);
            if (entity.isPresent()){
                updateData(entity.get(), obj);
                return repository.save(entity.get());
            }
            throw new EntityNotFoundException();
        } catch (EntityNotFoundException | NullPointerException e){
            throw new IdNotFoundExeption(id);
        }
    }

    public List<Address> findAllAddressesByCustomer(Long id){
        Optional<Customer> customer = repository.findById(id);
        if (customer.isEmpty()){
            throw new EntityNotFoundException();
        }

        List<Address> addresses = customer.get().getAddresses();
        if (addresses.isEmpty()){
            throw new NotFoundExeption("No address found");
        }
        return addresses;
    }

    public Address findAddressById(Long id){
        Optional<Address> address = addressRepository.findById(id);
        if(address.isEmpty()){
            throw new IdNotFoundExeption(id);
        }
        if(address.get().getDeleted()){
            throw new IdNotFoundExeption(id);
        }
        return address.get();
    }

    public Customer fromDTO(CustomerDTO objDTO){
        return new Customer(objDTO.getCustomerName(), objDTO.getDocumentNumber(),
                objDTO.getCustomerStatus(), objDTO.getCustomerType(), objDTO.getCreditScore());
    }

    private void updateData(Customer entity, Customer obj) {
        if (obj.getCustomerName() != null) {
            entity.setCustomerName(obj.getCustomerName());
        }
        if (obj.getDocumentNumber() != null) {
            entity.setDocumentNumber(obj.getDocumentNumber());
        }
        if (obj.getCustomerStatus() != null) {
            entity.setCustomerStatus(obj.getCustomerStatus());
        }
        if (obj.getCustomerType() != null) {
            entity.setCustomerType(obj.getCustomerType());
        }
        if (obj.getCreditScore() != null) {
            entity.setCreditScore(obj.getCreditScore());
        }
    }

    public void validateFields (Customer obj) {
        if(obj.getCustomerName().isBlank()){
            throw new IllegalArgumentException(INVALID_NAME_MESSAGE);
        }
        if(Long.toString(obj.getDocumentNumber()).length() != 11){
            throw new IllegalArgumentException(INVALID_DOCUMENT_MESSAGE);
        }
    }

}
