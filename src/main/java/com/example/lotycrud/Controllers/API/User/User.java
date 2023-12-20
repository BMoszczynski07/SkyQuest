package com.example.lotycrud.Controllers.API.User;

import com.example.lotycrud.Builders.UserBuilder;
import com.example.lotycrud.Models.Response.ResponseDTO;
import com.example.lotycrud.Models.User.UserDTO;
import com.example.lotycrud.Models.User.UserDataDTO;
import com.example.lotycrud.Repositories.UserRepository;
import com.example.lotycrud.Utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class User {
    private final UserRepository jdbc;

    @Autowired
    public User (UserRepository jdbc) {
        this.jdbc = jdbc;
    }

    @PostMapping("/api/login")
    public ResponseDTO userLogin(@RequestBody UserDTO user) {
        try {
            List<UserBuilder> users = jdbc.findUser(user.login);

            if (users.size() == 0) return new ResponseDTO<String>(404, "Nie znaleziono użytkownika o takiej nazwie");

            UserBuilder findUser = users.get(0);

            if (!PasswordUtil.matchPassword(user.pass, findUser.getPass())) return new ResponseDTO<String>(401, "Nieprawidłowe hasło");

            return new ResponseDTO<UserBuilder>(200, findUser);
        } catch (Exception e) {
            return new ResponseDTO<String>(500, "Wystąpił problem techniczny -> " + e.getMessage());
        }
    }

    @PostMapping("/api/register")
    public ResponseDTO userRegister(@RequestBody UserDataDTO user) {
        try {
            List<UserBuilder> findUser = jdbc.findUser(user.login);

            if (findUser.size() != 0) return new ResponseDTO(404, "User o takim loginie już istnieje");

            UserBuilder newUser = new UserBuilder();

            String hashPassword = PasswordUtil.encodePassword(user.pass);

            newUser.setName(user.firstname);
            newUser.setLastname(user.lastname);
            newUser.setNickname(user.login);
            newUser.setRoleId(2);
            newUser.setEmail(user.email);
            newUser.setPassPlain(user.pass);
            newUser.setPass(hashPassword);

            jdbc.insertUser(newUser);

            return new ResponseDTO(200, "Utworzono użytkownika. Możesz teraz się zalogować");
        } catch (Exception e) {
            return new ResponseDTO(500, "Wystąpił problem techniczny -> " + e.getMessage());
        }
    }
}
