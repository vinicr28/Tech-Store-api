package com.ericsson.rampup.entities;

import com.ericsson.rampup.entities.enums.Authorities;
import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_user")
@SQLDelete(sql = "UPDATE tb_user SET deleted = 1, email = 'deleted', password = 'deleted' WHERE id=?")
@Where(clause = "deleted=false")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Base.class)
    private Long id;
    @Email
    @NotBlank
    @JsonView(View.Base.class)
    private String email;
    @NotBlank
    private String password;
    private Boolean deleted = Boolean.FALSE;

    @JsonIgnoreProperties("user")
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "customer_id")
    @JsonView(View.Base.class)
    private Customer customer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonView(View.Base.class)
    private List<Role> roles = new ArrayList<>();

    public User() {
    }

    public User( Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.getRoles().add(new Role(Authorities.Operator));
    }

    public User( String email, String password) {
        this.email = email;
        this.password = password;
        this.getRoles().add(new Role(Authorities.Admin));
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role){
        this.getRoles().add(role);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /*
     * FOR SECURITY (CHANGE THE AUTHORITIES OF A USER TO A STRING LIST)
     */
    @JsonIgnore
     public String[] getRolesinString() {
         String[] rolesId = new String[roles.size()];
         Integer index = 0;

         for (Role i : roles) {
         rolesId[index++] = i.getAuthorityinString();
         }
         return rolesId;
     }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
