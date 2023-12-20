package com.example.lotycrud.Repositories;

import com.example.lotycrud.Builders.SaveFlightBuilder;
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

    public List<SaveFlightBuilder> findSavedFlight(int UserId, int FlightId) {
        String sql = "SELECT * FROM savedflights WHERE UserId = ? AND FlightId = ?";

        return jdbc.query(sql, new Object[]{UserId, FlightId}, new FlightRowMapper());
    }

    public void saveFlight(SaveFlightBuilder newFlight) {
        String sql = "INSERT INTO savedflights (UserId, FlightId) VALUES (?, ?)";

        jdbc.update(sql, newFlight.getUserId(), newFlight.getFlightId());
    }
}
