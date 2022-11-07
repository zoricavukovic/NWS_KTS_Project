package com.example.serbUber.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import static com.example.serbUber.util.Constants.*;

@Entity
@Table(name="ROLE")
public class Role implements GrantedAuthority {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    public Role(){}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDriver(){

        return this.name.equalsIgnoreCase(ROLE_DRIVER);
    }

    public boolean isAdmin(){

        return this.name.equalsIgnoreCase(ROLE_ADMIN);
    }

    public boolean isRegularUser(){

        return this.name.equalsIgnoreCase(ROLE_REGULAR_USER);
    }


}
