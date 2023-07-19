package com.ericsson.rampup.services;

import com.ericsson.rampup.dto.UserDTO;
import com.ericsson.rampup.entities.Role;
import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.entities.enums.Authorities;
import com.ericsson.rampup.integration.MailSending;
import com.ericsson.rampup.repositories.RoleRepository;
import com.ericsson.rampup.repositories.UserRepository;
import com.ericsson.rampup.services.exceptions.EmailAlreadyRegisteredExeption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private MailSending mailSending;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User us1,us2;
    private Sort sort;
    private Pageable pageable;

    @BeforeEach
    public void loadUsers(){
        us1 = new User(1L, "test@gmail.com", "12345");
        us2 = new User(2L, "test2@gmail.com", "12345");
        us1.addRole(new Role(Authorities.Operator));
        us2.addRole(new Role(Authorities.Operator));
        sort = Sort.by(Sort.Direction.ASC, "id");
        pageable = PageRequest.of(0, 10, sort);
    }

    @Test
    void givenFindAll_whenThereAreAList_ReturnList() {
        List<User> list = new ArrayList<>();
        list.add(us1);
        list.add(us2);
        Page<User> page = new PageImpl<>(list);
        //when
        when(userRepository.findAll(pageable)).thenReturn(page);
        userService.findAll(0);
        //then
        assertEquals(list, userService.findAll(0));
    }

    @Test
    void givenId_whenThereIsAMatchedId_thenReturnUser() {
        //given
        Long id = 2L;
        User expected = new User(2L, "mockTest@gmail.com", "mockTest");

        //when
        when(userRepository.findById(2L)).thenReturn(Optional.of(expected));

        //then
        assertEquals(expected, userService.findById(id));
    }

    @Test
    void givenUser_whenInsert_thenVerifyIfUserWasSaved() throws MessagingException {
        //given
        //when
        userService.insert(new UserDTO(us1));

        //then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(argumentCaptor.capture());

        User userCaptured = argumentCaptor.getValue();

        assertEquals(us1, userCaptured);
    }

    @Test
    void givenEmptyEmail_whenInsert_thenThrowIllegalArgumentExcption() {
        //given
        User user = new User(1L, "  ", "1234");
        user.addRole(new Role(Authorities.Operator));

        //when
        //then
        assertThatThrownBy(() -> userService.insert(new UserDTO(user)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must insert a valid email.");

    }

    @Test
    void givenEmptyPassword_whenInsert_thenThrowIllegalArgumentExcption() {
        //given
        User user = new User(1L, "test@gmail.com", "");
        user.addRole(new Role(Authorities.Operator));

        //when
        //then
        assertThatThrownBy(() -> userService.insert(new UserDTO(user)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must insert a valid password.");


    }

    @Test
    void givenEmail_whenEmailIsTaken_thenthrowExcption() {
        //given
        List<User> list = new ArrayList<>();
        list.add(us1);
        list.add(us2);

        User user = new User(3L, "test@gmail.com", "12345"); //user com email igual a outro user da lista
        user.addRole(new Role(Authorities.Operator));

        //when
        when(userRepository.findAll()).thenReturn(list);
        //then
        assertThrows(EmailAlreadyRegisteredExeption.class, () -> userService.insert(new UserDTO(user)));


    }

    @Test
    void delete_whenThereAreAnUser_thenReturnDeletedTrue() {
        //given
        Long id = 1L;
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(us1));
        userService.delete(id);
        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(userRepository).deleteById(argumentCaptor.capture());

        Long userIdCaptured = argumentCaptor.getValue();

        assertEquals(us1.getId(), userIdCaptured);

    }

    @Test
    void givenIdAndObj_whenUpdateUserInfo_thenUpdateFields() {
        //given
        Long id = 1L;
        User obj = new User(null, "testeUpdate@update", "4321");

        //when
        when(userRepository.getReferenceById(1L)).thenReturn(us1);
        userService.update(id,obj);

        //then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(argumentCaptor.capture());

        User userCaptured = argumentCaptor.getValue();

        assertEquals(us1, userCaptured);
    }

    @Test
    void givenUserDto_whenFromDtoToUser_thenCompare() {
        //given
        UserDTO userDto = new UserDTO(us1);

        //when
        User afterDto = userService.fromDTO(userDto);

        //then
        assertEquals(us1, afterDto);
    }
}