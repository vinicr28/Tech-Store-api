package com.ericsson.rampup.services;

import com.ericsson.rampup.dto.OrderDTO;
import com.ericsson.rampup.dto.OrderItemDTO;
import com.ericsson.rampup.dto.ReportOrderWeekDTO;
import com.ericsson.rampup.entities.*;
import com.ericsson.rampup.repositories.*;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import com.ericsson.rampup.services.exceptions.NotForSaleExeption;
import com.ericsson.rampup.services.exceptions.NotFoundExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductOfferingRepository productRepository;

    @Autowired
    private RequestService requestService;

    private static final Integer DURATION_LIMIT = 1800; //seconds


    public List<Order> findAll(int page){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable =  PageRequest.of(page, 10, sort);
        return repository.findAll(pageable).stream().toList();
    }

    public List<Order> findAllFilter(Long id){
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            throw new IdNotFoundExeption(id);
        }

        return user.get().getCustomer().getOrders();
    }

    public Order findById(Long id){
        Optional<Order> obj = repository.findById(id);
        return obj.orElseThrow(() -> new IdNotFoundExeption(id));
    }

    public Order insert(OrderDTO objDto){

        Optional<Customer> customerOpt = customerRepository.findById(objDto.getCustomerId());
        Optional<Address> addressOpt = addressRepository.findById(objDto.getAddressId());


        if(customerOpt.isEmpty() || addressOpt.isEmpty()){
            throw new NotFoundExeption("Optinal is empty");
        }

        Order obj = new Order();
        repository.save(obj);

        Customer customer = customerOpt.get();
        Address address = addressOpt.get();

        customer.getOrders().add(obj);
        customerRepository.save(customer);

        obj.setCustomer(customer);
        obj.setDeliveryAddress(address);
        repository.save(obj);

        for (OrderItemDTO product : objDto.getOrderItem()) {
            Optional<ProductOffering> productOpt = productRepository.findById(product.getId());

            ProductOffering productOffering = productOpt.get();

            if(productOffering.isNotForSale()){
                throw new NotForSaleExeption("Product out of stock: " + productOffering.getProductName() +
                        ", id: " + productOffering.getId());
            }

            OrderItem item = new OrderItem(obj, productOffering, product.getQuantity(), objDto.getDiscount());
            orderItemRepository.save(item);

            obj.getItems().add(item);
        }

        return repository.save(obj);
    }

    public void delete(Long id){
        Order order = findById(id);
        Duration duration = Duration.between(order.getInstant(), Instant.now());
        if(duration.toSeconds() <= DURATION_LIMIT){
            repository.deleteById(id);
        }else {
            requestService.insert(id);
        }
    }

    public Order update(Long id, Order obj){
        try{
            Order entity = repository.getReferenceById(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException | NullPointerException e){
            throw new IdNotFoundExeption(id);
        }

    }

    private void updateData(Order entity, Order obj) {
        if(obj.getDeliveryAddress() != null){
            entity.setDeliveryAddress(obj.getDeliveryAddress());
            entity.setInstant(Instant.now());
        }
    }

    public int countToday(){
        List<Object[]> count = repository.countToday();

        if(count != null && !count.isEmpty()){
            return Integer.parseInt(count.get(0)[0].toString());
        }
        return 0;
    }

    public double invoicing(){
        List<Double> count = orderItemRepository.invoicing();

        if(count != null && !count.isEmpty()){
            double totalInvoice = 0.0;

            for (Double total: count) {
                totalInvoice += total;
            }
            return totalInvoice;
        }
        return 0.0;
    }

    public List<ReportOrderWeekDTO> orderChar(){
        SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");

        Calendar now = Calendar.getInstance();
        now.set(Calendar.DAY_OF_WEEK, now.getFirstDayOfWeek());

        List<Object[]> ordersPerDay = repository.orderChar(yearMonthDay.format(now.getTime()));

        List<ReportOrderWeekDTO> list = new ArrayList<>();

        if(ordersPerDay != null && !ordersPerDay.isEmpty()){
            for (Object[] dayOrder: ordersPerDay) {
                list.add(new ReportOrderWeekDTO(Integer.parseInt(dayOrder[1].toString()) - 1, Integer.parseInt(dayOrder[0].toString())));
            }
        }
        return list;
    }
}
