package com.ericsson.rampup.services;

import com.ericsson.rampup.entities.Order;
import com.ericsson.rampup.entities.Request;
import com.ericsson.rampup.repositories.OrderRepository;
import com.ericsson.rampup.repositories.RequestRepository;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Request> findAll(int page){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable =  PageRequest.of(page, 10, sort);
        return repository.findAll(pageable).stream().toList();
    }

    public Request findById(Long id){
        Optional<Request> obj = repository.findById(id);
        return obj.orElseThrow(() -> new IdNotFoundExeption(id));
    }

    public Request insert(Long id){
        Optional<Order> obj = orderRepository.findById(id);

        if(obj.isEmpty()){
            throw new IdNotFoundExeption(id);
        }

        Request request = new Request(obj.get());
        return repository.save(request);
    }

    public void Delete(Long id){
        Request request = findById(id);
        Order order = request.getOrder();
        repository.deleteById(id);
        orderRepository.deleteById(order.getId());
    }

}
