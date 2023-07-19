package com.ericsson.rampup.services;

import com.ericsson.rampup.entities.ProductOffering;
import com.ericsson.rampup.entities.enums.PoState;
import com.ericsson.rampup.repositories.ProductOfferingRepository;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOfferingService {

    @Autowired
    private ProductOfferingRepository repository;




    public List<ProductOffering> findAll(int page){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable =  PageRequest.of(page, 10, sort);
        return repository.findAllByDeleted(pageable, false).stream().toList();
    }

    public ProductOffering findById(Long id){
        Optional<ProductOffering> obj = repository.findById(id);
        return obj.orElseThrow(() -> new IdNotFoundExeption(id));
    }

    public ProductOffering insert(ProductOffering obj){

        if(obj.getProductName().isBlank()){
            throw new IllegalArgumentException("Must insert a valid product name!");
        }

        return repository.save(obj);
    }

    public void delete(Long id){
        findById(id);
        repository.deleteById(id);
    }

    @Transactional
    public ProductOffering update(Long id, ProductOffering obj){
        try{
            ProductOffering entity = repository.getReferenceById(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException e){
            throw new IdNotFoundExeption(id);
        }

    }

    private void updateData(ProductOffering entity, ProductOffering obj) {
        if(obj.getProductName() != null){
            entity.setProductName(obj.getProductName());
        }
        if (obj.getUnitPrice() != null){
            entity.setUnitPrice(obj.getUnitPrice());
        }
        if (obj.isNotForSale() != null){
            entity.setSellIndicator(obj.isNotForSale());
        }
        if(obj.getState() != null){
            entity.setState(obj.getState());
        }
    }

    public int totalOfProducts(){
        List<Object[]> count = repository.totalOfProducts();

        if(count != null && !count.isEmpty()){
            return Integer.parseInt(count.get(0)[0].toString());
        }
        return 0;
    }

}
