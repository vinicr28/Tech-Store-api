package com.ericsson.rampup.repositories;

import com.ericsson.rampup.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAll(Pageable pageable);

    @Query(value = "select count(*) from tb_order where instant > CURRENT_TIMESTAMP -1 AND deleted = 0", nativeQuery = true)
    List<Object[]> countToday();

    @Query(value = "select count(*), to_char(instant, 'D') from tb_order o " +
            "where o.instant >= to_timestamp(?1, 'YYYY-MM-DD') and o.deleted = 0 " +
            "group by to_char(instant, 'D') order by to_char(instant, 'D')", nativeQuery = true)
    List<Object[]> orderChar(String startDate);
}
