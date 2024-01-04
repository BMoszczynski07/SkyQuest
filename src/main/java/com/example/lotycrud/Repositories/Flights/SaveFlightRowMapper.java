package com.example.lotycrud.Repositories.Flights;

import com.example.lotycrud.Builders.SaveFlightBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SaveFlightRowMapper implements RowMapper<SaveFlightBuilder> {
    @Override
    public SaveFlightBuilder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        SaveFlightBuilder saveFlightBuilder = new SaveFlightBuilder();

        saveFlightBuilder.setUserId(resultSet.getInt("UserId"));
        saveFlightBuilder.setFlightId(resultSet.getInt("FlightId"));

        return saveFlightBuilder;
    }
}
