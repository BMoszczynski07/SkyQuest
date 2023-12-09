package com.example.lotycrud.Controllers.API;

import com.example.lotycrud.Models.ResponseDTO;
import com.example.lotycrud.Models.UserDTO;
import com.example.lotycrud.Models.UserDataDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User {
    @PostMapping("/api/login")
    public ResponseDTO userLogin(@RequestBody UserDTO user) {
        try {

        } catch (Exception e) {
            return new ResponseDTO(500, e.getMessage());
        }
    }

    @PostMapping("/api/register")
    public ResponseDTO userRegister(@RequestBody UserDataDTO user) {
        return new ResponseDTO(200, "success");
    }
}
