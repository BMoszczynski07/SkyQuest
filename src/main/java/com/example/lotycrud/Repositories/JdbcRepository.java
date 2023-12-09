package com.example.lotycrud.Repositories;

import com.example.lotycrud.Builders.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public JdbcRepository (JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<UserBuilder> findUser(String name) {
        String sql = "SELECT Name, LastName, NickName, RoleId, Pass FROM user WHERE NickName = ?";

        return jdbc.query(sql, new Object[]{name}, new UserRowMapper());
    }
}
