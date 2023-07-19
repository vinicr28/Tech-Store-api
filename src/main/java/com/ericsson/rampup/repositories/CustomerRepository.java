package com.ericsson.rampup.repositories;

import com.ericsson.rampup.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAll(Pageable pageable);

    Page<Customer> findAllByDeleted(Pageable pageable, Boolean deleted);
}
