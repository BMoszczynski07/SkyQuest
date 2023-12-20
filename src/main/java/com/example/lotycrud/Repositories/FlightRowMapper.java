package com.example.lotycrud.Repositories;

import com.example.lotycrud.Builders.SaveFlightBuilder;
import com.example.lotycrud.Builders.UserBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightRowMapper implements RowMapper<SaveFlightBuilder> {
    @Override
    public SaveFlightBuilder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        SaveFlightBuilder saveFlightBuilder = new SaveFlightBuilder();

        saveFlightBuilder.setUserId(resultSet.getInt("UserId"));
        saveFlightBuilder.setFlightId(resultSet.getInt("FlightId"));

        return saveFlightBuilder;
    }
}
