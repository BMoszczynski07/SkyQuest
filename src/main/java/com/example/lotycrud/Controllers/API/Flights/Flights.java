package com.example.lotycrud.Controllers.API.Flights;
import com.example.lotycrud.Builders.FlightBuilder;
import com.example.lotycrud.Builders.GetFlightBuilder;
import com.example.lotycrud.Builders.SaveFlightBuilder;
import com.example.lotycrud.Models.Flights.FindFlightsDTO;
import com.example.lotycrud.Models.Response.ResponseDTO;
import com.example.lotycrud.Repositories.Flights.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class Flights {
    private final FlightRepository jdbc;

    @Autowired
    public Flights(FlightRepository jdbc) {
        this.jdbc = jdbc;
    }

    @PostMapping("/api/add-flights/{flightsQty}")
    public ResponseDTO addFlight(@PathVariable int flightsQty) {
        try {
            for (int i = 0; i < flightsQty; i++) {
                boolean isTwoWay = Math.random() > 0.10;

                FlightBuilder newFlight = new FlightBuilder();

                // TODO: get all planes' id and choose one
                List<Integer> planesId = jdbc.getPlanesId();
                int planeId = (int) Math.floor(Math.random() * (planesId.size() - 1 - 0 + 1) + 0);

                // all airports' id
                List<Integer> flightsId = jdbc.getAirportsId();

                // TODO: get all flights' airport and choose one for arrive
                int randFlightArrive = (int) Math.floor(Math.random() * (flightsId.size() - 1 - 0 + 1) + 0);
                int flightIdArrive = flightsId.get(randFlightArrive);

                // TODO: get all flights' airport and choose one for departure
                int randFlightDeparture = (int) Math.floor(Math.random() * (flightsId.size() - 1 - 0 + 1) + 0);
                int flightIdDeparture = flightsId.get(randFlightDeparture);

                // TODO: random Gate and Terminal
                int randGate = (int) Math.floor(Math.random() * 71);
                int randTerminal = (int) Math.floor(Math.random() * 4);

                // TODO: randomArriveTime and randomDepartureTime
                Random random = new Random();
                LocalDateTime startDateTime = LocalDateTime.now();

                int randomDays = random.nextInt(365) + 1;

                int randomHoursForDeparture = random.nextInt(24) + 1;
                int randomHoursForArrive = random.nextInt(randomHoursForDeparture + 3) + 1;

                int randomMinutesForDeparture = random.nextInt(59) + 1;
                int randomMinutesForArrive = random.nextInt(randomMinutesForDeparture + 59) + 1;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime datePlusDaysDeparture = startDateTime.plusDays(randomDays);
                LocalDateTime datePlusHoursDeparture = datePlusDaysDeparture.plusHours(randomHoursForDeparture);
                LocalDateTime datePlusMinutesDeparture = datePlusHoursDeparture.plusMinutes(randomMinutesForDeparture);
                String dateDeparture = datePlusMinutesDeparture.format(formatter);

                LocalDateTime datePlusDaysArrive = startDateTime.plusDays(randomDays);
                LocalDateTime datePlusHoursArrive = datePlusDaysArrive.plusHours(randomHoursForArrive);
                LocalDateTime datePlusMinutesArrive = datePlusHoursArrive.plusMinutes(randomMinutesForArrive);
                String dateArrive = datePlusMinutesArrive.format(formatter);

                System.out.println(randomDays);
                System.out.println(randomHoursForDeparture);
                System.out.println(randomHoursForArrive);
                System.out.println();
                System.out.println(randomMinutesForDeparture);
                System.out.println(randomMinutesForArrive);

                newFlight.setPlaneId(planeId);
                newFlight.setArrive(flightIdArrive);
                newFlight.setArriveDateTime(dateArrive.toString());
                newFlight.setDeparture(flightIdDeparture);
                newFlight.setDepartureDateTime(dateDeparture.toString());
                newFlight.setGate(randGate);
                newFlight.setTerminal(randTerminal);

                System.out.println(dateDeparture);
                System.out.println(dateArrive);
            }

            return new ResponseDTO<String>(200, "Dodano loty");
        } catch (Exception e) {
            return new ResponseDTO<String>(500, "Wystąpił problem techniczny -> " + e.getMessage());
        }
    }

    @PostMapping("/api/get-flights")
    public ResponseDTO getFlight(@RequestBody FindFlightsDTO flights) {
        try {
            List<GetFlightBuilder> selectedFlights = jdbc.findFlight(flights.departureDateTime, flights.arriveDateTime, flights.departureAirport, flights.arriveAirport, flights.twoWay);

            if (selectedFlights.size() == 0) return new ResponseDTO<String>(404, "Lotów nie znaleziono");

            return new ResponseDTO<List<GetFlightBuilder>>(200, selectedFlights);
        } catch (Exception e) {
            return new ResponseDTO<String>(500, "Wystąpił problem techniczny " + e.getMessage());
        }
    }

    // TODO: Make an endpoint fetching all saved flights


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
