package com.ericsson.rampup.services;

import com.ericsson.rampup.entities.Role;
import com.ericsson.rampup.entities.enums.Authorities;
import com.ericsson.rampup.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role r1,r2;
    private Sort sort;
    private Pageable pageable;

    @BeforeEach
    public void loadRoles(){
        r1 = new Role(Authorities.Admin);
        r2 = new Role(Authorities.Operator);

        r1.setId(1L);
        r2.setId(2L);

        sort = Sort.by(Sort.Direction.ASC, "id");
        pageable = PageRequest.of(0, 10, sort);
    }


    @Test
    void givenListOfRole_whenUseFindAll_thenReturnAllRoles() {
        //given
        List<Role> expected = new ArrayList<>();
        expected.add(r1);
        expected.add(r2);
        Page<Role> page = new PageImpl<>(expected);

        //when
        Mockito.when(roleRepository.findAll(pageable)).thenReturn(page);
        List<Role> actual = roleService.findAll(0);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void findById_whenSearchWithValidId_thenReturnEntireRole() {
        //given
        Long id = 1L;

        //when
        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(r1));
        roleService.findById(id);

        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(roleRepository).findById(argumentCaptor.capture());

        Long idCaptured = argumentCaptor.getValue();

        assertEquals(id, idCaptured);
        assertEquals(r1, roleService.findById(id));

    }
}