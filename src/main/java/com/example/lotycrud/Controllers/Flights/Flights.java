package com.example.lotycrud.Controllers.Flights;
import com.example.lotycrud.Builders.SaveFlightBuilder;
import com.example.lotycrud.Models.Response.ResponseDTO;
import com.example.lotycrud.Repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Flights {
    private final FlightRepository jdbc;

    @Autowired
    public Flights(FlightRepository jdbc) {
        this.jdbc = jdbc;
    }

    @PostMapping("/api/add-flight/{UserId}/{FlightId}")
    public ResponseDTO addFlight(@PathVariable int UserId, @PathVariable int FlightId) {
        try {
           List<SaveFlightBuilder> findFlight = jdbc.findSavedFlight(UserId, FlightId);

           if (findFlight.size() != 0) return new ResponseDTO<String>(500, "Już zapisałeś ten lot");

           SaveFlightBuilder newFlight = new SaveFlightBuilder();

           newFlight.setUserId(UserId);
           newFlight.setFlightId(FlightId);

           jdbc.saveFlight(newFlight);

           return new ResponseDTO<String>(200, "Zapisano lot");
        } catch (Exception e) {
            return new ResponseDTO<String>(500, "Wystąpił problem techniczny -> " + e.getMessage());
        }
    }
}
