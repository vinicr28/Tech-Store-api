package com.ericsson.rampup.services;

import com.ericsson.rampup.dto.CustomerDTO;
import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.entities.User;
import com.ericsson.rampup.entities.enums.CustomerType;
import com.ericsson.rampup.repositories.CustomerRepository;
import com.ericsson.rampup.repositories.UserRepository;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import com.ericsson.rampup.services.exceptions.NotFoundExeption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerService customerService;

    private static final Long CUSTOMER_ID = 1L, USER_ID = 1L;
    private Customer c1,c2;
    private User user;
    private CustomerDTO customerDTO;
    private Sort sort;
    private Pageable pageable;

    @BeforeEach
    public void loadCustomer() {
        c1 = new Customer("vinicius Reis", 98765432112L, "Active", CustomerType.LegalPerson, "100");
        c2 = new Customer("Luiza", 12345678912L, "Active", CustomerType.NaturalPerson, "110");
        c1.setId(1L);
        c2.setId(2L);

        user = new User(USER_ID, "vinicius@gmail.com", "1234ericsson#");
        user.setCustomer(c1);

        customerDTO = new CustomerDTO(c1);
        customerDTO.setUserId(USER_ID);

        sort = Sort.by(Sort.Direction.ASC, "id");
        pageable = PageRequest.of(0, 10, sort);
    }

    @Test
    void givenFindAll_whenThereAreCustomers_thenReturnAll() {
        //given
        List<Customer> list = new ArrayList<>(Arrays.asList(c1,c2));
        Page<Customer> page = new PageImpl<>(list);

        //when
        when(customerRepository.findAllByDeleted(pageable,false)).thenReturn(page);

        //then
        assertEquals(list, customerService.findAll(0));
    }

    @Test
    void givenAnId_whenFindById_thenReturnCustomer() {
        //given
        //when
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(c1));

        //then
        assertEquals(c1, customerService.findById(CUSTOMER_ID));
    }

    @Test
    void givenAnIdAndBody_whenInsert_thenVerifyUsageAndCustomer() {
        //given
        User user1 = new User(null, "teste@gmail.com", "123");
        //when
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user1));
        when(customerRepository.save(any())).thenReturn(c1);
        Customer actual = customerService.insert(customerDTO);

        //then
        verify(userRepository, times(1)).save(user1);
        verify(customerRepository, times(1)).save(any());
        assertEquals(c1.getDocumentNumber(), actual.getDocumentNumber());
    }

    @Test
    void givenAnId_whenDelete_thenVerifyIfDeleted() {
        //given
        //when
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(c1));
        customerService.delete(CUSTOMER_ID);

        //then
        verify(customerRepository, times(1)).deleteById(CUSTOMER_ID);
    }

    @Test
    void givenAnIdAndBody_whenUpdate_thenVerifyIfEquals() {
        //given
        //when
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.ofNullable(c1));
        customerService.update(CUSTOMER_ID, c2);

        //then
        assertNotEquals(c2, c1);
        assertEquals(c2.getDocumentNumber(), c1.getDocumentNumber());
    }

//    Exceptions ->>>>>>>>>>>>>>>>>>>>

    @Test
    void givenNotExistingId_whenFindById_thenThrowIdNotFoundException() {
        //given
        Long id = 4L;

        //when
        //then
        assertThrows(NoSuchElementException.class, () -> customerService.findById(id));
    }

    @Test
    void givenNotExistingUser_whenInsert_thenThrowNotFoundException() {
        //given
        //when
        //then
        assertThrows(NotFoundExeption.class, () -> customerService.insert(customerDTO));
    }

    @Test
    void givenNotExistingId_whenUpdate_thenThrowIdNotFoundException(){
        //given
        Long id = 4L;

        //when
        //then
        assertThrows(IdNotFoundExeption.class, () -> customerService.update(id, c1));
    }

    @Test
    void givenBlankCustomerName_whenInsert_thenReturnIllegalArgumentException() {
        //given
        customerDTO.setCustomerName("");

        //when
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        //then
        assertThrows(IllegalArgumentException.class, () -> customerService.insert(customerDTO));
    }

    @Test
    void givenWrongFormatDocumentNumber_whenInsert_thenReturnIllegalArgumentException() {
        //given
        customerDTO.setDocumentNumber(1234L);

        //when
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        //then
        assertThrows(IllegalArgumentException.class, () -> customerService.insert(customerDTO));
    }
}