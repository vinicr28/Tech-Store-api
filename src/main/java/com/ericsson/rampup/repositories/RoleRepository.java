package com.ericsson.rampup.repositories;

import com.ericsson.rampup.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Page<Role> findAll(Pageable pageable);
}
