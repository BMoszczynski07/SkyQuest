package com.example.lotycrud.Controllers.API.User;

import com.example.lotycrud.Builders.UserBuilder;
import com.example.lotycrud.Models.Response.ResponseDTO;
import com.example.lotycrud.Models.User.UserDTO;
import com.example.lotycrud.Models.User.UserDataDTO;
import com.example.lotycrud.Repositories.JdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class User {
    private final JdbcRepository jdbc;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public User (JdbcRepository jdbc) {
        this.jdbc = jdbc;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean matchPass (String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @PostMapping("/api/login")
    public ResponseDTO userLogin(@RequestBody UserDTO user) {
        try {
            List<UserBuilder> users = jdbc.findUser(user.login);

            if (users.size() == 0) return new ResponseDTO<String>(404, "Nie znaleziono użytkownika o takiej nazwie");

            UserBuilder findUser = users.get(0);

            return new ResponseDTO<UserBuilder>(200, findUser);
        } catch (Exception e) {
            return new ResponseDTO<String>(500, e.getMessage());
        }
    }

    @PostMapping("/api/register")
    public ResponseDTO userRegister(@RequestBody UserDataDTO user) {
        return new ResponseDTO<String>(200, "success");
    }
}
