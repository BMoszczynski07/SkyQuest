package com.example.lotycrud.Builders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBuilder {
    private int id;
    private String name;
    private String lastname;
    private String nickname;
    private int roleId;
    private String pass;
}