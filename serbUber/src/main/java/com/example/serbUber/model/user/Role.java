package com.example.serbUber.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private String name;

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
}
