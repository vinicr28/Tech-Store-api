package com.ericsson.rampup.resources;

import com.ericsson.rampup.dto.UserDTO;
import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.services.UserService;
import com.ericsson.rampup.token.TokenMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class UserResourceTest {

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
    private UserService userService;

    private static final Long USER_ID = 1L;
    private static final String URL = "/users";
    private static final String URL_BY_ID = "/users/{id}";
    private User user1, user2;
    private UserDTO userDto;

    @BeforeEach
    public void loadUsers() throws Exception {
        user1 = new User(1L, "vinicius@gmail.com", "123456");
        user2 = new User(2L, "testeResource@gmail.com", "654321");

        userDto = new UserDTO(user1);

        User user = new User("admin@gmail.com", "$2a$10$egv82L9uxdvdOkEbnT4LdudlYpwK4jdTaG8P4lBizMd5QDOGwMUSe");
        when(userService.loadUserByUsername(any())).thenReturn(org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()).password(user.getPassword())
                .roles(user.getRolesinString()).build());

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(springSecurityFilterChain).build();
        tokenMock = new TokenMock();
        acessToken = tokenMock.obtainAcessToken("admin@gmail.com","123456", mockMvc);

    }

    @Test
    void givenFindAll_thenReturnOkAndUsers() throws Exception {
        //given
        List<User> list = new ArrayList<>(Arrays.asList(user1, user2));

        //when
        when(userService.findAll(0)).thenReturn(list);

        //then
        mockMvc.perform(get(URL+"/page/0").header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void givenId_whenFindById_thenReturnOkAndUser() throws Exception {
        //given
        //when
        when(userService.findById(USER_ID)).thenReturn(user1);

        //then
        mockMvc.perform(get(URL_BY_ID, USER_ID).header("Authorization", "Bearer " + acessToken))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    void givenDTO_whenInsert_thenReturnCreated() throws Exception {
        //given
        //when
        when(userService.insert(any())).thenReturn(user1);

        //then
        mockMvc.perform(post("/users/signup").header("Authorization", "Bearer " + acessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void givenAnId_whenDelete_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete(URL_BY_ID, USER_ID).header("Authorization", "Bearer " + acessToken))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).delete(USER_ID);
    }

    @Test
    void givenIdAndDTO_whenUpdate_thenReturnNoContent() throws Exception {
        //given
        //when
        when(userService.update(USER_ID, user1)).thenReturn(user1);

        //then
        mockMvc.perform(patch(URL_BY_ID, USER_ID).header("Authorization", "Bearer " + acessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNoContent());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

}

//    exceptions ->>>>>>>>>>>>>>>>>>>>


}