package com.example.lotycrud.Repositories.Flights;

import com.example.lotycrud.Builders.FlightBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightRowMapper implements RowMapper<FlightBuilder> {
    @Override
    public FlightBuilder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FlightBuilder newFlight = new FlightBuilder();

        newFlight.setPlaneId(resultSet.getInt("PlaneId"));
        newFlight.setArrive(resultSet.getInt("Arrive"));
        newFlight.setArriveDateTime(resultSet.getString("ArriveDateTime"));
        newFlight.setDeparture(resultSet.getInt("Departure"));
        newFlight.setDepartureDateTime(resultSet.getString("DepartureDateTime"));
        newFlight.setGate(resultSet.getInt("Gate"));
        newFlight.setTerminal(resultSet.getInt("Terminal"));

        return newFlight;
    }
}
