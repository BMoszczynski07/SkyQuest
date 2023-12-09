package com.example.lotycrud.Repositories;

import com.example.lotycrud.Builders.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserBuilder findUser(String name) {
        String sql = "SELECT NickName FROM user WHERE NickName = " + name;

        return jdbcTemplate.query(sql, new UserRowMapper());
    }
}
