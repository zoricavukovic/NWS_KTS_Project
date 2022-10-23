package com.example.serbUber.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class Role{

    private String name;

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

