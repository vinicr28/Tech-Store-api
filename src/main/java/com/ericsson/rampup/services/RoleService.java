package com.ericsson.rampup.services;

import com.ericsson.rampup.entities.Role;
import com.ericsson.rampup.repositories.RoleRepository;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;


    public List<Role> findAll(int page){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, 10, sort);
        return repository.findAll(pageable).stream().toList();
    }

    public Role findById(Long id){
        Optional<Role> obj = repository.findById(id);
        return obj.orElseThrow(() -> new IdNotFoundExeption(id));
    }

}
