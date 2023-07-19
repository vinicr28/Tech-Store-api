//package com.ericsson.rampup.resources;
//
//import com.ericsson.rampup.dto.OrderDTO;
//import com.ericsson.rampup.entities.*;
//import com.ericsson.rampup.entities.enums.AddressType;
//import com.ericsson.rampup.entities.enums.CustomerType;
//import com.ericsson.rampup.entities.enums.PoState;
//import com.ericsson.rampup.services.OrderService;
//import com.ericsson.rampup.token.TokenMock;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureMockMvc(addFilters = false)
//class OrderResourceTest {
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    @Autowired
//    private FilterChainProxy springSecurityFilterChain;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private MockMvc mockMvc;
//    private String acessToken;
//    private TokenMock tokenMock;
//
//    @MockBean
//    private OrderService orderService;
//
//    private static final Long ORDER_ID = 1L;
//    private static final String URL = "/orders";
//    private static final String URL_BY_ID = "/orders/{id}";
//    private Order o1, o2;
//    private Customer customer;
//    private Address address;
//    private ProductOffering product;
//    private OrderDTO orderDTO;
//    private OrderItem orderItem;
//
//    @BeforeEach
//    public void loadOrders() throws Exception {
//        o1 = new Order();
//        o1.setId(1L);
//
//        o2 = new Order();
//        o2.setId(2L);
//
//        customer = new Customer("vinicius Reis", 98765432112L, "Active", CustomerType.LegalPerson, "100");
//        address = new Address("Rua Gesselina", 162, "JD Esplendor", 13338244, "Brasil", AddressType.HomeAddress);
//        product = new ProductOffering("TV SAMSUNG", 1590.90,true, PoState.Active);
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
//
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
//        tokenMock = new TokenMock();
//        acessToken = tokenMock.obtainAcessToken("admin@gmail.com","123456", mockMvc);
//
//    }
//
//    @Test
//    void givenFindAll_thenReturnAllOrders() throws Exception {
//        //given
//        List<Order> list = new ArrayList<>(Arrays.asList(o1,o2));
//        //when
//        when(orderService.findAll(0)).thenReturn(list);
//        //then
//        mockMvc.perform(get(URL+"/page/0").header("Authorization", "Bearer " + acessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id", is(1)));
//    }
//
//    @Test
//    void givenAnId_whenFindById_thenReturnOrder() throws Exception {
//        //given
//        //when
//        when(orderService.findById(ORDER_ID)).thenReturn(o1);
//        //then
//        mockMvc.perform(get(URL_BY_ID, ORDER_ID).header("Authorization", "Bearer " + acessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)));
//    }
//
//    @Test
//    void insert_shouldReturnCreated() throws Exception {
//        //given
//        //when
//        when(orderService.insert(any())).thenReturn(o1);
//        //then
//        mockMvc.perform(post(URL).header("Authorization", "Bearer " + acessToken)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(orderDTO)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void givenAnId_whenDelete_thenReturnNoContent() throws Exception {
//        mockMvc.perform(delete(URL_BY_ID, ORDER_ID).header("Authorization", "Bearer " + acessToken))
//                .andExpect(status().isNoContent());
//        verify(orderService, times(1)).delete(ORDER_ID);
//    }
//
//    @Test
//    void update_shouldReturnOk() throws Exception {
//        //given
//        //when
//        when(orderService.update(ORDER_ID, o2)).thenReturn(o1);
//        //then
//        mockMvc.perform(patch(URL_BY_ID, ORDER_ID).header("Authorization", "Bearer " + acessToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(o2)))
//                .andExpect(status().isOk());
//    }
//}