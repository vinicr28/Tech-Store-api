package com.ericsson.rampup.repositories;

import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.entities.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findAll(Pageable pageable);
}
