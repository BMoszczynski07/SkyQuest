package com.example.lotycrud.Repositories;

import com.example.lotycrud.Builders.FlightBuilder;
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

    public List<FlightBuilder> findFlight(String departureDateTime, String arriveDateTime, String airportDeparture, String airportArrive) {
        String sql = "SELECT F.Id AS Id, A1.Name AS ArriveAirportName, A1.airportSign AS ArriveAirportSign, " +
                "F.ArriveDateTime AS ArriveDateTime, " +
                "A2.Name AS DepartureAirportName, A2.airportSign AS DepartureAirportSign, " +
                "F.DepartureDateTime AS DepartureDateTime, F.Gate AS Gate, F.Terminal AS Terminal, " +
                "P.Number AS Model " +
                "FROM airport AS A1 " +
                "INNER JOIN flight AS F ON A1.Id = F.Arrive " +
                "INNER JOIN airport AS A2 ON A2.Id = F.Departure " +
                "INNER JOIN plane as P ON P.Number = F.PlaneId " +
                "WHERE F.DepartureDateTime >= ? AND F.ArriveDateTime >= ? " +
                "AND A1.Sign = ? AND A2.Sign = ? " +
                "ORDER BY F.DepartureDateTime";

        return jdbc.query(sql, new Object[]{departureDateTime, arriveDateTime, airportDeparture, airportArrive}, new FindFlightRowMapper());
    }


    public List<SaveFlightBuilder> findSavedFlight(int UserId, int FlightId) {
        String sql = "SELECT * FROM savedflights WHERE UserId = ? AND FlightId = ?";

        return jdbc.query(sql, new Object[]{UserId, FlightId}, new FlightRowMapper());
    }

    public boolean isUserExists(int UserId) {
        String sql = "SELECT COUNT(*) FROM user WHERE Id = ?";
        int count = jdbc.queryForObject(sql, Integer.class, UserId);
        return count > 0;
    }

    public boolean saveFlight(SaveFlightBuilder newFlight) {
        if (!isUserExists(newFlight.getUserId())) {
            throw new RuntimeException("Użytkownik o podanym id nie istnieje!");
        }

        String sql = "INSERT INTO savedflights (UserId, FlightId) VALUES (?, ?)";

        try {
            jdbc.update(sql, newFlight.getUserId(), newFlight.getFlightId());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Wystąpił problem podczas zapisu lotu: " + e.getMessage());
        }
    }
}
