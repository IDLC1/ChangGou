package com.tom.domain;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String address;

    public User(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
