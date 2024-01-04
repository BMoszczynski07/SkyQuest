package com.example.lotycrud.Repositories.User;

import com.example.lotycrud.Builders.UserBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserBuilder> {
    @Override
    public UserBuilder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserBuilder userBuilder = new UserBuilder();

        userBuilder.setName(resultSet.getString("Name"));
        userBuilder.setLastname(resultSet.getString("LastName"));
        userBuilder.setNickname(resultSet.getString("NickName"));

        userBuilder.setPass(resultSet.getString("Pass"));

        userBuilder.setRoleId(resultSet.getInt("RoleId"));

        return userBuilder;
    }
}
