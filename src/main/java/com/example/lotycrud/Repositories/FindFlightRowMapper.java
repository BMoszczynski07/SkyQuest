package com.example.lotycrud.Repositories;

import com.example.lotycrud.Builders.FlightBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FindFlightRowMapper implements RowMapper<FlightBuilder> {
    @Override
    public FlightBuilder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FlightBuilder findFlightBuilder = new FlightBuilder();

        findFlightBuilder.setId(resultSet.getInt("Id"));
        findFlightBuilder.setGate(resultSet.getInt("Gate"));
        findFlightBuilder.setTerminal(resultSet.getInt("Terminal"));
        findFlightBuilder.setArriveAirportName(resultSet.getString("ArriveAirportName"));
        findFlightBuilder.setArriveAirportSign(resultSet.getString("ArriveAirportSign"));
        findFlightBuilder.setArriveDateTime(resultSet.getTimestamp("ArriveDateTime").toString());
        findFlightBuilder.setDepartureAirportName(resultSet.getString("DepartureAirportName"));
        findFlightBuilder.setDepartureAirportSign(resultSet.getString("DepartureAirportSign"));
        findFlightBuilder.setDepartureDateTime(resultSet.getTimestamp("DepartureDateTime").toString());
        findFlightBuilder.setModel(resultSet.getString("Model"));

        return findFlightBuilder;
    }
}
