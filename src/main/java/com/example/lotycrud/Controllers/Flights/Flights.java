package com.example.lotycrud.Controllers.Flights;
import com.example.lotycrud.Builders.FlightBuilder;
import com.example.lotycrud.Builders.SaveFlightBuilder;
import com.example.lotycrud.Models.Response.ResponseDTO;
import com.example.lotycrud.Repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/api/get-flight/{departureDateTime}/{arriveDateTime}/{departureAirport}/{arriveAirport}")
    public ResponseDTO getFlight(@PathVariable String departureDateTime, @PathVariable String arriveDateTime, @PathVariable String departureAirport, @PathVariable String arriveAirport) {
        try {
            List<FlightBuilder> selectedFlights = jdbc.findFlight(departureDateTime, arriveDateTime, departureAirport, arriveAirport);

            if (selectedFlights.size() == 0) return new ResponseDTO<String>(404, "Lotów nie znaleziono");

            return new ResponseDTO<List<FlightBuilder>>(200, selectedFlights);
        } catch (Exception e) {
            return new ResponseDTO<String>(500, "Wystąpił problem techniczny " + e.getMessage());
        }
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
