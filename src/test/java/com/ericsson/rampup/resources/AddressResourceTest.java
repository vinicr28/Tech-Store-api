package com.ericsson.rampup.resources;

import com.ericsson.rampup.dto.AddressDTO;
import com.ericsson.rampup.entities.Address;
import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.entities.enums.AddressType;
import com.ericsson.rampup.entities.enums.CustomerType;
import com.ericsson.rampup.services.AddressService;
import com.ericsson.rampup.token.TokenMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressResourceTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private String acessToken;
    private TokenMock tokenMock;

    @MockBean
    private AddressService addressService;

    private static final Long ADDRESS_ID = 1L, CUSTOMER_ID = 1L;
    private static final Integer PAGE_NUM = 0;
    private static final String PAGE ="/page/{page}";
    private static final String URL = "/addresses";
    private static final String URL_BY_ID = "/addresses/{id}";
    private Address a1,a2;
    private AddressDTO addressDTO;
    private Customer customer;

    @BeforeEach
    public void loadAddresses() throws Exception {
        a1 = new Address("Rua Gesselina", 162, "JD Esplendor", 13338244, "Brasil", AddressType.HomeAddress);
        a2 = new Address("Rua 24 de maio", 100, "Esplanada", 13335500, "Brasil", AddressType.ShippingAddress);
        a1.setId(1L);
        a2.setId(2L);

        customer = new Customer("teste mock", 1234567L, "Active", CustomerType.LegalPerson, "10");
        customer.setId(CUSTOMER_ID);

        addressDTO = new AddressDTO(a1);
        addressDTO.setCustomerId(CUSTOMER_ID);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
        tokenMock = new TokenMock();
        acessToken = tokenMock.obtainAcessToken("admin@gmail.com","123456", mockMvc);
    }

    @Test
    void givenFindAll_thenReturnAllAddresses() throws Exception {
        //given
        List<Address> list = new ArrayList<>(Arrays.asList(a1,a2));

        //when
        when(addressService.findAll(PAGE_NUM)).thenReturn(list);

        //then
        mockMvc.perform(get(URL + PAGE, PAGE_NUM).header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void givenAnId_whenFindById_thenReturnAddress() throws Exception {
        //given
        //when
        when(addressService.findById(ADDRESS_ID)).thenReturn(a1);
        //then
        mockMvc.perform(get(URL_BY_ID, ADDRESS_ID).header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
//    Exceptions ->>>>>>>>>>>>>>>>
}