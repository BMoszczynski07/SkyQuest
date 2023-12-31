package com.example.lotycrud.Repositories.User;

import com.example.lotycrud.Builders.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public UserRepository (JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<UserBuilder> findUser(String name) {
        String sql = "SELECT Id, Name, LastName, NickName, RoleId, Pass FROM user WHERE NickName = ?";

        return jdbc.query(sql, new Object[]{name}, new UserRowMapper());
    }

    public void insertUser(UserBuilder newUser) {
        String sql = "INSERT INTO user (Email, Name, LastName, NickName, RoleId, Pass, PassPlain) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql, newUser.getEmail(), newUser.getName(), newUser.getLastname(), newUser.getNickname(), newUser.getRoleId(), newUser.getPass(), newUser.getPassPlain());
    }
}
