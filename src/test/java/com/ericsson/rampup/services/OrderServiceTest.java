//package com.ericsson.rampup.services;
//
//import com.ericsson.rampup.dto.OrderDTO;
//import com.ericsson.rampup.entities.*;
//import com.ericsson.rampup.entities.enums.AddressType;
//import com.ericsson.rampup.entities.enums.CustomerType;
//import com.ericsson.rampup.entities.enums.PoState;
//import com.ericsson.rampup.repositories.*;
//import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
//import com.ericsson.rampup.services.exceptions.NotForSaleExeption;
//import com.ericsson.rampup.services.exceptions.NotFoundExeption;
//import org.aspectj.weaver.ast.Or;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @Mock
//    private AddressRepository addressRepository;
//
//    @Mock
//    private ProductOfferingRepository productRepository;
//
//    @Mock
//    private OrderItemRepository orderItemRepository;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    private static final Long ORDER_ID = 1L, CUSTOMER_ID = 1L, ADDRESS_ID = 1L, PRODUCT_ID = 1L;
//    private Order o1, o2;
//    private Customer customer;
//    private Address address;
//    private ProductOffering product;
//    private OrderDTO orderDTO;
//    private OrderItem orderItem;
//
//    @BeforeEach
//    public void loadOrders() {
//        o1 = new Order();
//        o1.setId(1L);
//
//        o2 = new Order();
//        o2.setId(2L);
//
//        customer = new Customer("vinicius Reis", 98765432112L, "Active", CustomerType.LegalPerson, "100");
//        address = new Address("Rua Gesselina", 162, "JD Esplendor", 13338244, "Brasil", AddressType.HomeAddress);
//        product = new ProductOffering("TV SAMSUNG", 1590.90,false, PoState.Active);
//
//        customer.setId(1L);
//        address.setId(1L);
//        product.setId(1L);
//
//        orderDTO = new OrderDTO(o1);
//        orderDTO.setDiscount(0.0);
//        orderDTO.setQuantity(1);
//        orderDTO.setCustomerId(1L);
//        orderDTO.setAddressId(1L);
//        orderDTO.setProductId(1L);
//
//        orderItem = new OrderItem(o1, product, orderDTO.getQuantity(), orderDTO.getDiscount());
//
//        o1.setDeliveryAddress(address);
//        o1.setCustomer(customer);
//    }
//
//    @Test
//    void givenList_whenFindAll_thenReturnList() {
//        //given
//        List<Order> list = new ArrayList<>(Arrays.asList(o1,o2));
//        Page<Order> page = new PageImpl<>(list);
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
//        Pageable pageable =  PageRequest.of(0, 10, sort);
//
//        //when
//        when(orderRepository.findAll(pageable)).thenReturn(page);
//
//        //then
//        assertEquals(list, orderService.findAll(0));
//
//    }
//
//    @Test
//    void givenAnId_whenFindById_thenReturnOrder() {
//        //given
//        //when
//        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(o1));
//
//        //then
//        assertEquals(o1, orderService.findById(ORDER_ID));
//    }
//
//    @Test
//    void givenOrderDTO_whenInsert_thenVerifyUsageAndIfEqual() {
//        //given
//        //when
//        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
//        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(address));
//        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
//        when(orderItemRepository.save(any())).thenReturn(orderItem);
//        when(orderRepository.save(any())).thenReturn(o1);
//        Order acutal = orderService.insert(orderDTO);
//        //then
//
//        verify(orderRepository, times(3)).save(any());
//        verify(customerRepository, times(1)).save(any());
//        verify(orderItemRepository, times(1)).save(any());
//        assertEquals(o1, acutal);
//    }
//
//    @Test
//    void givenAnId_whenDelete_thenVerifyIfDeleted() {
//        //given
//        //when
//        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(o1));
//        orderService.delete(ORDER_ID);
//
//        //then
//        verify(orderRepository, times(1)).deleteById(ORDER_ID);
//    }
//
//    @Test
//    void givenAnIdAndBody_whenUpdate_thenVerifyIfEqual() {
//        //given
//        o2.setCustomer(new Customer());
//        //when
//        when(orderRepository.getReferenceById(2L)).thenReturn(o2);
//        orderService.update(2L, o1);
//
//        //then
//        assertEquals(o1.getDeliveryAddress(),o2.getDeliveryAddress());
//    }
//
////    Exceptions ->>>>>>>>>>>>>>>>>>>>>
//
//    @Test
//    void givenNotExistingId_whenFindById_thenReturnIdNotFoundException() {
//        //given
//        Long id = 4L;
//        //when
//        //then
//        assertThrows(IdNotFoundExeption.class, () -> orderService.findById(id));
//    }
//
//    @Test
//    void givenEmptyOptionals_whenInsert_thenReturnNotFoundException() {
//        assertThrows(NotFoundExeption.class, () -> orderService.insert(orderDTO));
//    }
//
//    @Test
//    void givenProductForSaleTrue_whenInsert_thenNotForSaleException() {
//        //given
//        product.setSellIndicator(true);
//        //when
//        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
//        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(address));
//        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
//        //then
//        assertThrows(NotForSaleExeption.class, () -> orderService.insert(orderDTO));
//    }
//
//    @Test
//    void givenNotExistingId_whenUpdate_thenThrowIdNotFoundExeption() {
//        //given
//        Long id = 4L;
//        //when
//        //then
//        assertThrows(IdNotFoundExeption.class, () -> orderService.update(id,o1));
//    }
//
//}