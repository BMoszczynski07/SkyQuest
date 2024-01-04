package com.example.lotycrud.Repositories.Flights;

import com.example.lotycrud.Builders.GetFlightBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FindFlightRowMapper implements RowMapper<GetFlightBuilder> {
    @Override
    public GetFlightBuilder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GetFlightBuilder findFlightBuilder = new GetFlightBuilder();

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
