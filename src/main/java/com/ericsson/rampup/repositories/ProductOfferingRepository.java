package com.ericsson.rampup.repositories;

import com.ericsson.rampup.entities.ProductOffering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductOfferingRepository extends JpaRepository<ProductOffering, Long> {
    Page<ProductOffering> findAll(Pageable pageable);

    Page<ProductOffering> findAllByDeleted(Pageable pageable, Boolean deleted);

    @Query(value = "select count(*) from tb_product_offering where deleted = 0", nativeQuery = true)
    List<Object[]> totalOfProducts();
}
