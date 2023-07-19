package com.ericsson.rampup.config;

import com.ericsson.rampup.entities.*;
import com.ericsson.rampup.entities.enums.AddressType;
import com.ericsson.rampup.entities.enums.Authorities;
import com.ericsson.rampup.entities.enums.CustomerType;
import com.ericsson.rampup.entities.enums.PoState;
import com.ericsson.rampup.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductOfferingRepository productOfferingRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RoleRepository rolerRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {

        ProductOffering p1 = new ProductOffering("Notebook", 850.50, false, PoState.Active);
        ProductOffering p2 = new ProductOffering("TV", 4500d, false, PoState.Active);
        ProductOffering p3 = new ProductOffering("Smartphone", 1200.0, false, PoState.Active);
        ProductOffering p4 = new ProductOffering("Headphones", 150.0, false, PoState.Active);
        ProductOffering p5 = new ProductOffering("Tablet", 500.0, false, PoState.Active);
        ProductOffering p6 = new ProductOffering("Smartwatch", 250.0, false, PoState.Active);
        ProductOffering p7 = new ProductOffering("Gaming Console", 400.0, false, PoState.Active);
        ProductOffering p8 = new ProductOffering("Wireless Speaker", 100.0, false, PoState.Active);
        ProductOffering p9 = new ProductOffering("External Hard Drive", 80.0, false, PoState.Active);
        ProductOffering p10 = new ProductOffering("Digital Camera", 600.0, false, PoState.Active);
        ProductOffering p11 = new ProductOffering("Printer", 200.0, false, PoState.Active);
        ProductOffering p12 = new ProductOffering("VR Headset", 300.0, false, PoState.Active);
        ProductOffering p13 = new ProductOffering("Wireless Mouse", 50.0, false, PoState.Active);
        ProductOffering p14 = new ProductOffering("Power Bank", 30.0, false, PoState.Active);
        ProductOffering p15 = new ProductOffering("Monitor", 350.0, false, PoState.Active);

        productOfferingRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15));



        User us1 = new User("admin@gmail.com", "$2a$10$egv82L9uxdvdOkEbnT4LdudlYpwK4jdTaG8P4lBizMd5QDOGwMUSe");
        User us2 = new User(null,"operator@gmail.com", "$2a$10$X326KmkHAg1BiAeg.QkoxedR87M.REPMDWr0RXUQIuErqrD1A1ygC");
        rolerRepository.saveAll(Arrays.asList(us1.getRoles().get(0),us2.getRoles().get(0)));
        userRepository.saveAll(Arrays.asList(us1,us2));

        Customer c1 = new Customer("vinicius", 44984842822L, "active", CustomerType.LegalPerson, "10");
        Customer c2 = new Customer("Luiza", 44984842821L, "active", CustomerType.NaturalPerson, "90");
        customerRepository.saveAll(Arrays.asList(c1,c2));

        us1.setCustomer(c1);
        c1.setUser(us1);
        c1.setPassword(us1.getPassword());
        us2.setCustomer(c2);
        c2.setUser(us2);
        c2.setPassword(us2.getPassword());
        userRepository.saveAll(Arrays.asList(us1, us2));
        customerRepository.saveAll(Arrays.asList(c1,c2));


        Address a1 = new Address("teste rua", 162, "teste bairro", 13338245, "Brasil", AddressType.HomeAddress);
        Address a2 = new Address("teste rua 2", 100, "teste bairro 2", 13338299, "Brasil", AddressType.BusinessAddress);
        Address a3 = new Address("teste rua 3", 100, "teste bairro 3", 13338299, "Brasil", AddressType.BusinessAddress);


        a1.setCustomer(c1);
        a2.setCustomer(c2);
        a3.setCustomer(c1);
        addressRepository.saveAll(Arrays.asList(a1,a2,a3));

        c1.addAddress(a1);
        c1.addAddress(a3);
        c2.addAddress(a2);
        customerRepository.saveAll(Arrays.asList(c1,c2));

        Order o1 = new Order();
        Order o2 = new Order();

        o1.setCustomer(c1);
        o1.setDeliveryAddress(c1.getAddresses().get(0));
        o2.setCustomer(c2);
        o2.setDeliveryAddress(c2.getAddresses().get(0));
        o1.setInstant(Instant.now().minus(1, ChronoUnit.DAYS));
        o2.setInstant(Instant.now().minus(2, ChronoUnit.DAYS));
        orderRepository.saveAll(Arrays.asList(o1,o2));

        OrderItem order1 = new OrderItem(o1,p3,2,10d);
        OrderItem order2 = new OrderItem(o1,p2,1,0d);
        OrderItem order3 = new OrderItem(o2,p3,5,0d);
        orderItemRepository.saveAll(Arrays.asList(order1,order2,order3));


    }
}
