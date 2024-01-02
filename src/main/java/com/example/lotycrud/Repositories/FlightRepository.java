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
        String sql = "SELECT flight.Id, A1.Name AS ArriveAirportName, A1.airportSign AS ArriveAirportSign, flight.ArriveDateTime, " +
                "A2.Name AS DepartureAirportName, A2.airportSign AS DepartureAirportSign, flight.DepartureDateTime, flight.Gate, flight.Terminal, " +
                "P.Model " +
                "FROM flight " +
                "INNER JOIN airport A1 ON flight.Arrive = A1.Id " +
                "INNER JOIN airport A2 ON flight.Departure = A2.Id " +
                "INNER JOIN plane P ON flight.PlaneId = P.Number " +
                "WHERE flight.DepartureDateTime >= ? AND flight.ArriveDateTime >= ? AND " +
                "A1.airportSign = ? AND A2.airportSign = ? " +
                "ORDER BY flight.DepartureDateTime";

        List<FlightBuilder> result = jdbc.query(sql, new Object[]{departureDateTime, arriveDateTime, airportDeparture, airportArrive}, new FindFlightRowMapper());

        return result;
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
