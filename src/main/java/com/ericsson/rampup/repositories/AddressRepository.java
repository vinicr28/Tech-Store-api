package com.ericsson.rampup.repositories;

import com.ericsson.rampup.entities.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findAllByDeleted(Pageable pageable, Boolean deleted);
}
