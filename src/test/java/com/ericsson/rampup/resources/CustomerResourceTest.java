package com.ericsson.rampup.resources;

import com.ericsson.rampup.dto.CustomerDTO;
import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.entities.enums.CustomerType;
import com.ericsson.rampup.services.CustomerService;
import com.ericsson.rampup.token.TokenMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerResourceTest {

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
    private CustomerService customerService;

    private static final Long CUSTOMER_ID = 2L;
    private static final String URL = "/customers";
    private static final String URL_BY_ID = "/customers/{id}";
    private CustomerDTO customerDTO1, customerDTO2;
    private Customer c1, c2;
    private User user;

    @BeforeEach
    public void loadCustomers() throws Exception {
        c1 = new Customer("vinicius Reis", 98765432112L, "Active", CustomerType.LegalPerson, "100");
        c2 = new Customer("Luiza", 12345678912L, "Active", CustomerType.NaturalPerson, "110");
        c1.setId(1L);
        c2.setId(2L);

        user = new User(1L,"vinicius@gmail.com", "1234567");

        customerDTO1 = new CustomerDTO(c1);
        customerDTO2 = new CustomerDTO(c2);
        customerDTO1.setUserId(1L);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
        tokenMock = new TokenMock();
        acessToken = tokenMock.obtainAcessToken("admin@gmail.com","123456", mockMvc);

    }

    @Test
    void givenFindAll_thenReturnAllCustomers() throws Exception {
        //given
        List<Customer> list = new ArrayList<>(Arrays.asList(c1,c2));
        //when
        when(customerService.findAll(0)).thenReturn(list);
        //then
        mockMvc.perform(get(URL + "/page/0").header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void givenAnId_whenFindById_thenReturnCustomer() throws Exception {
        //given
        //when
        when(customerService.findById(CUSTOMER_ID)).thenReturn(c2);
        //then
        mockMvc.perform(get(URL_BY_ID, CUSTOMER_ID).header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    void givenACustomerDTO_whenInsert_thenReturnCreated() throws Exception {
        //given
        //when
        when(customerService.insert(any())).thenReturn(c1);
        //then
        mockMvc.perform(post(URL).header("Authorization", "Bearer " + acessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO1)))
                .andExpect(status().isCreated());
    }

    @Test
    void givenAnId_whenDelete_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete(URL_BY_ID, CUSTOMER_ID).header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isNoContent());
        verify(customerService, times(1)).delete(CUSTOMER_ID);
    }

    @Test
    void givenAnIdAndACustomer_whenUpdate_thenReturnOk() throws Exception {
        //given
        //when
        when(customerService.update(CUSTOMER_ID, c1)).thenReturn(c2);
        //then
        mockMvc.perform(patch(URL_BY_ID, CUSTOMER_ID).header("Authorization", "Bearer " + acessToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(c2)))
                .andExpect(status().isOk());
    }
}