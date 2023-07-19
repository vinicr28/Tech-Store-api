package com.ericsson.rampup.resources;

import com.ericsson.rampup.entities.ProductOffering;
import com.ericsson.rampup.entities.enums.PoState;
import com.ericsson.rampup.services.ProductOfferingService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductOfferingResourceTest {

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
    private ProductOfferingService productOfferingService;

    private static final Long PRODUCT_ID = 1L;
    private static final Integer PAGE_NUM = 0;
    private static final String PAGE ="/page/{page}";
    private static final String URL = "/productOfferings";
    private static final String URL_BY_ID = "/productOfferings/{id}";
    private ProductOffering p1,p2;

    @BeforeEach
    public void loadProducts() throws Exception {
        p1 = new ProductOffering("TV SAMSUNG", 5880.90, false, PoState.Active);
        p2 = new ProductOffering("GELADEIRA FRIO", 4000D, false, PoState.Active);
        p1.setId(1L);
        p2.setId(2L);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
        tokenMock = new TokenMock();
        acessToken = tokenMock.obtainAcessToken("admin@gmail.com","123456", mockMvc);

    }

    @Test
    void givenFindAll_thenReturnAllProducts() throws Exception {
        //given
        List<ProductOffering> list = new ArrayList<>(Arrays.asList(p1,p2));

        //when
        when(productOfferingService.findAll(PAGE_NUM)).thenReturn(list);

        //then
        mockMvc.perform(get(URL + PAGE, PAGE_NUM).header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void givenAnId_whenFindById_thenReturnOKAndProduct() throws Exception {
        //given
        //when
        when(productOfferingService.findById(PRODUCT_ID)).thenReturn(p1);

        //then
        mockMvc.perform(get(URL_BY_ID, PRODUCT_ID).header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

//    @Test
//    void givenProduct_whenInsert_thenReturnCreated() throws Exception {
//        //given
//        //when
//        when(productOfferingService.insert(any())).thenReturn(p1);
//
//        //then
//        mockMvc.perform(post("/productOfferings").header("Authorization", "Bearer " + acessToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(p1)))
//                .andExpect(status().isCreated());
//    }

    @Test
    void givenId_whenDelete_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete(URL_BY_ID, PRODUCT_ID).header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isNoContent());
        verify(productOfferingService, times(1)).delete(PRODUCT_ID);
    }

    @Test
    void givenAnIdAndProduct_whenUpdate_thenReturnOk() throws Exception {
        //given
        //when
        when(productOfferingService.update(PRODUCT_ID, p2)).thenReturn(p2);

        //then
        mockMvc.perform(patch(URL_BY_ID, PRODUCT_ID).header("Authorization", "Bearer " + acessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(p2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is(p2.getProductName())));
    }
}