package com.ericsson.rampup.resources;

import com.ericsson.rampup.entities.Role;
import com.ericsson.rampup.entities.enums.Authorities;
import com.ericsson.rampup.services.RoleService;
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

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false) //anula os filtros do JWT
class RoleResourceTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;
    private String acessToken;
    private TokenMock tokenMock;

    @MockBean
    private RoleService roleService;

    private static final Long ROLE_ID = 1L;
    private Role role1, role2;

    @BeforeEach
    public void loadRoles() throws Exception {
        role1 = new Role(Authorities.Admin);
        role2 = new Role(Authorities.Operator);
        role1.setId(1L);
        role2.setId(2L);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
        tokenMock = new TokenMock();
        acessToken = tokenMock.obtainAcessToken("admin@gmail.com","123456", mockMvc);

    }

    @Test
    void givenFindAll_thenReturnAllRoles() throws Exception {
        //given
        List<Role> list = new ArrayList<>(Arrays.asList(role1, role2));

        //when
        when(roleService.findAll(0)).thenReturn(list);

        //then
        mockMvc.perform(get("/roles/page/0").header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void givenAnId_whenThereIsAMatch_thenReturnRole() throws Exception {
        //given
        //when
        when(roleService.findById(ROLE_ID)).thenReturn(role1);

        //then
        mockMvc.perform(get("/roles/1").header("Authorization", "Bearer " + acessToken))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.authorities", is("Admin")));
    }
}