package com.ericsson.rampup.entities;

import com.ericsson.rampup.entities.enums.Authorities;
import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_role")
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonView(View.Base.class)
    private Authorities authorities;

    public Role() {
    }

    public Role(Authorities authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Authorities getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authorities authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /*
     * CODE FOR SECURITY (RETURN AUTHORITY IN STRING)
     */
    @JsonIgnore
     public String getAuthorityinString() {
        String authorityString = String.valueOf(authorities.ordinal());
        return authorityString;
     }
}
