package com.example.lotycrud.Repositories.Flights;

import com.example.lotycrud.Builders.FlightBuilder;
import com.example.lotycrud.Builders.GetFlightBuilder;
import com.example.lotycrud.Builders.SaveFlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FlightRepository {
    private JdbcTemplate jdbc;

    @Autowired
    public FlightRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // TODO: planes' id
    public List<Integer> getPlanesId() {
        String sql = "SELECT Number FROM plane";

        return jdbc.queryForList(sql, Integer.class);
    }

    // TODO: flights' arrives and departures id
    public List<Integer> getAirportsId() {
        String sql = "SELECT Id FROM airport";

        return jdbc.queryForList(sql, Integer.class);
    }

    public boolean addFlight(FlightBuilder newFlight) {
            String sql = "INSERT INTO flight (Id," +
                    "PlaneId," +
                    "Arrive," +
                    "ArriveDateTime," +
                    "Departure," +
                    "DepartureDateTime," +
                    "Gate," +
                    "Terminal) " +
                    "VALUES (?,?,?,?,?,?,?,?)";

            jdbc.update(sql, new Object[]{
                    newFlight.getPlaneId(),
                    newFlight.getArrive(),
                    newFlight.getArriveDateTime(),
                    newFlight.getDeparture(),
                    newFlight.getDepartureDateTime(),
                    newFlight.getGate(),
                    newFlight.getTerminal(),
            }, new FlightBuilder());

            return true;
    }

    public List<GetFlightBuilder> findFlight(String departureDateTime, String arriveDateTime, String airportDeparture, String airportArrive, boolean twoWay) {
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

        if (!twoWay) {
            List<GetFlightBuilder> result = jdbc.query(sql, new Object[]{departureDateTime, arriveDateTime, airportArrive, airportDeparture}, new FindFlightRowMapper());

            return result;
        }

        List<GetFlightBuilder> result1 = jdbc.query(sql, new Object[]{departureDateTime, arriveDateTime, airportArrive, airportDeparture}, new FindFlightRowMapper());
        List<GetFlightBuilder> result2 = jdbc.query(sql, new Object[]{arriveDateTime, departureDateTime, airportDeparture, airportArrive}, new FindFlightRowMapper());

        List<GetFlightBuilder> result = Stream.concat(result1.stream(), result2.stream())
                .collect(Collectors.toList());

        return result;
    }


    public List<SaveFlightBuilder> findSavedFlight(int UserId, int FlightId) {
        String sql = "SELECT * FROM savedflights WHERE UserId = ? AND FlightId = ?";

        return jdbc.query(sql, new Object[]{UserId, FlightId}, new SaveFlightRowMapper());
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
