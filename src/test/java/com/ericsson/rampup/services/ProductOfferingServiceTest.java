package com.ericsson.rampup.services;

import com.ericsson.rampup.entities.ProductOffering;
import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.entities.enums.PoState;
import com.ericsson.rampup.repositories.ProductOfferingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductOfferingServiceTest {

    @Mock
    private ProductOfferingRepository productOfferingRepository;

    @InjectMocks
    private ProductOfferingService productOfferingService;

    private ProductOffering p1;
    private ProductOffering p2;

    private static final Long PRODUCT_ID = 1L;

    @BeforeEach
    public void loadProduct() {
        p1 = new ProductOffering("TV SAMSUNG", 1590.90,true,PoState.Active);
        p1.setId(1L);

        p2 = new ProductOffering("IPAD PRO", 3000.00, true, PoState.Active);
        p2.setId(2L);
    }

//    @Test
//    void givenProductList_whenFindAll_thenReturnAllProducts() {
//        //given
//        List<ProductOffering> list = new ArrayList<>(Arrays.asList(p1,p2));
//        Page<ProductOffering> page = new PageImpl<>(list);
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
//        Pageable pageable =  PageRequest.of(0, 10, sort);
//
//        //when
//        when(productOfferingRepository.findAll(pageable)).thenReturn(page);
//
//        //then
//        assertEquals(list, productOfferingService.findAll(0));
//        Mockito.verify(productOfferingRepository, times(1)).findAll(pageable);
//    }

    @Test
    void givenId_whenIdExist_thenReturnProduct() {
        //given
        //when
        when(productOfferingRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(p1));
        ProductOffering actual = productOfferingService.findById(PRODUCT_ID);

        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(productOfferingRepository, times(1)).findById(argumentCaptor.capture());

        Long idCaptured = argumentCaptor.getValue();

        assertEquals(PRODUCT_ID, idCaptured);
        assertEquals(p1, actual);
    }

    @Test
    void givenInsertBody_whenAllFieldsAreTrue_returnCreated() {
        //given
        //when
        when(productOfferingRepository.save(p1)).thenReturn(p1);
        ProductOffering actual = productOfferingService.insert(p1);

        //then
        assertEquals(p1, actual);
    }

    @Test
    void givenProductNameBlank_whenInsert_thenThrowIllegalArgumentException() {
        //given
        p1.setProductName("");
        //when
        //then
        assertThrows(IllegalArgumentException.class,() -> productOfferingService.insert(p1));
    }

    @Test
    void givenId_whenDelete_thenVerifyIfIdIsTheSame() {
        //given
        //when
        when(productOfferingRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(p1));
        productOfferingService.delete(PRODUCT_ID);
        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productOfferingRepository).findById(argumentCaptor.capture());
        Long idDeleted = argumentCaptor.getValue();
        System.out.println(idDeleted);

        assertEquals(PRODUCT_ID, idDeleted);
    }

    @Test
    void givenProduct_whenUpdateP1_thenVerifyIfIsEqual() {
        //given
        ProductOffering productUpdate = new ProductOffering("PANELA UPDATE", 50.59, true, PoState.Active);

        //when
        when(productOfferingRepository.getReferenceById(PRODUCT_ID)).thenReturn(p1);
        productOfferingService.update(PRODUCT_ID, productUpdate);

        //then
        assertEquals(productUpdate.getProductName(), p1.getProductName());
        assertEquals(productUpdate.getUnitPrice(), p1.getUnitPrice());
    }
}