package com.example.lotycrud.Builders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBuilder {
    private int id;
    private String email;
    private String name;
    private String lastname;
    private String nickname;
    private int roleId;
    private String pass;
    private String passPlain;
}