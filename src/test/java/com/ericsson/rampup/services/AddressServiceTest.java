package com.ericsson.rampup.services;

import com.ericsson.rampup.dto.AddressDTO;
import com.ericsson.rampup.entities.Address;
import com.ericsson.rampup.entities.Customer;
import com.ericsson.rampup.entities.enums.AddressType;
import com.ericsson.rampup.entities.enums.CustomerType;
import com.ericsson.rampup.repositories.AddressRepository;
import com.ericsson.rampup.repositories.CustomerRepository;
import com.ericsson.rampup.services.exceptions.IdNotFoundExeption;
import com.ericsson.rampup.services.exceptions.NotFoundExeption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AddressService addressService;

    private static final Long ADDRESS_ID = 1L;
    private static final Long CUSTOMER_ID = 3L;
    private Address a1, a2;
    private Customer customer;
    private AddressDTO addressDTO;

    @BeforeEach
    public void loadAddresses(){
        a1 = new Address("Rua Gesselina", 162, "JD Esplendor", 13338244, "Brasil", AddressType.HomeAddress);
        a2 = new Address("Rua 24 de maio", 100, "Esplanada", 13335500, "Brasil", AddressType.ShippingAddress);

        a1.setId(1L);
        a2.setId(2L);

        customer = new Customer("teste mock", 1234567L, "Active", CustomerType.LegalPerson, "10");
        customer.setId(CUSTOMER_ID);

        addressDTO = new AddressDTO(a1);
        addressDTO.setCustomerId(CUSTOMER_ID);
    }

    @Test
    void givenFindAll_whenThereAreAddresses_thenReturnAllAddresses() {
        //given
        Page<Address> page = new PageImpl<>(Arrays.asList(a1,a2));
        List<Address> list = new ArrayList<>(Arrays.asList(a1,a2));
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable =  PageRequest.of(0, 10, sort);

        //when
        when(addressRepository.findAllByDeleted(pageable, false)).thenReturn(page);

        //then
        assertEquals(list, addressService.findAll(0));
    }

    @Test
    void givenAnId_whenFindById_thenReturnAddress() {
        //given
        //when
        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(a1));

        //then
        assertEquals(a1, addressService.findById(ADDRESS_ID));
    }

    @Test
    void givenAddressDTO_whenInsert_thenReturnAddress() {
        //given
        //when
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(addressRepository.save(any())).thenReturn(a1);
        Address actual = addressService.insert(addressDTO);

        //then
        verify(customerRepository, times(1)).save(customer);
        assertEquals(a1, actual);

    }

    @Test
    void givenAnId_whenDeleteById_thenDelete() {
        //given
        //when
        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(a1));
        addressService.delete(ADDRESS_ID);

        //then
        verify(addressRepository, times(1)).deleteById(ADDRESS_ID);

    }

    @Test
    void givenAnIdAndABody_whenUpdate_thenReturnAddressUpdated() {
        //given
        a1.setCustomer(customer);
        customer.getAddresses().add(a1);
        //when
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));
        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.ofNullable(a1));
        addressService.update(a1.getCustomer().getId(),ADDRESS_ID, a2);

        //then
        assertNotEquals(a2, a1);
        assertEquals(a2.getStreet(), a1.getStreet());
    }

//    Exceptions ->>>>>>>>>>>>>>>>>>>>>

    @Test
    void givenNotExistingId_whenFindById_thenReturnIdNotFoundExeption() {
        //given
        Long id = 4L;
        //when
        //then
        assertThrows(IdNotFoundExeption.class, () -> addressService.findById(id));
    }

    @Test
    void givenNotExistingCustomer_whenInsert_thenThrowNotFoundException() {
        //given
        //when
        //then
        assertThrows(NotFoundExeption.class, () -> addressService.insert(addressDTO));
    }

    @Test
    void givenBlankStreet_whenInsertAddress_thenThrowIllegalArgumentException() {
        //given
        addressDTO.setStreet("");

        //when
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        //then
        assertThrows(IllegalArgumentException.class, () -> addressService.insert(addressDTO));
    }

    @Test
    void givenHouseNumberLessThenOne_whenInsertAddress_thenThrowIllegalArgumentException() {
        //given
        addressDTO.setHouseNumber(0);

        //when
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        //then
        assertThrows(IllegalArgumentException.class, () -> addressService.insert(addressDTO));
    }

    @Test
    void givenBlankNeighborhood_whenInsertAddress_thenThrowIllegalArgumentException() {
        //given
        addressDTO.setNeighborhood(" ");

        //when
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        //then
        assertThrows(IllegalArgumentException.class, () -> addressService.insert(addressDTO));
    }

    @Test
    void givenBlankCountry_whenInsertAddress_thenThrowIllegalArgumentException() {
        //given
        addressDTO.setCountry("");

        //when
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        //then
        assertThrows(IllegalArgumentException.class, () -> addressService.insert(addressDTO));
    }

    @Test
    void givenNotExistingId_whenUpdate_thenThrowIdNotFoundException() {
        //given
        Long id = 4L;
        //when
        //then
        assertThrows(NullPointerException.class, () -> addressService.update(a1.getCustomer().getId(),id, a1));
    }
}