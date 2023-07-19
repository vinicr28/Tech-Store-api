package com.ericsson.rampup.repositories;

import com.ericsson.rampup.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(value = "select total_price from tb_order_item oi " +
            "JOIN tb_order o on oi.order_id = o.id " +
            "where o.deleted = 0", nativeQuery = true)
    List<Double> invoicing();

}
