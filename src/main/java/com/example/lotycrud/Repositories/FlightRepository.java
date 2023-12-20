package com.example.lotycrud.Repositories;

import com.example.lotycrud.Builders.SaveFlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlightRepository {
    private JdbcTemplate jdbc;

    @Autowired
    public FlightRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
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
