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

    public void insertFlight(int planeId, int flightIdArrive, String arriveDateTime, int flightIdDeparture, String departureDateTime, int gate, int terminal) {
        FlightBuilder newFlight = new FlightBuilder();

        newFlight.setPlaneId(planeId);
        newFlight.setArrive(flightIdArrive);
        newFlight.setArriveDateTime(arriveDateTime);
        newFlight.setDeparture(flightIdDeparture);
        newFlight.setDepartureDateTime(departureDateTime);
        newFlight.setGate(gate);
        newFlight.setTerminal(terminal);

        jdbc.addFlight(newFlight);
    }

    public ArrayList<String> getDates() {
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Losowanie czasu odlotu 1
        LocalDateTime startDateTime = LocalDateTime.now();

        int randomDaysForDate1 = random.nextInt(365) + 1;

        LocalDateTime datePlusDaysDeparture1 = startDateTime.plusDays(randomDaysForDate1);
        String dateDeparture1 = datePlusDaysDeparture1.format(formatter);

        // Losowanie czasu przylotu 1
        int randomHoursForArrive1 = random.nextInt(5) + 1;
        int randomMinutesForArrive1 = random.nextInt(30) + 1;

        LocalDateTime datePlusHoursArrive1 = datePlusDaysDeparture1.plusHours(randomHoursForArrive1);
        LocalDateTime datePlusMinutesArrive1 = datePlusHoursArrive1.plusMinutes(randomMinutesForArrive1);
        String dateArrive1 = datePlusMinutesArrive1.format(formatter);

        ArrayList<String> dates = new ArrayList<String>();

        // Losowanie czasu odlotu 2
        int randomDaysForDate2 = randomDaysForDate1 + random.nextInt(365) + 1;

        LocalDateTime datePlusDaysDeparture2 = datePlusMinutesArrive1.plusDays(randomDaysForDate2);
        String dateDeparture2 = datePlusDaysDeparture2.format(formatter);

        // Losowanie czasu przylotu 2
        int randomHoursForDeparture2 = random.nextInt(5) + 1;
        int randomMinutesForDeparture2 = random.nextInt(30) + 1;

        LocalDateTime datePlusHoursArrive2 = datePlusDaysDeparture2.plusHours(randomHoursForDeparture2);
        LocalDateTime datePlusMinutesArrive2 = datePlusHoursArrive2.plusMinutes(randomMinutesForDeparture2);
        String dateArrive2 = datePlusMinutesArrive2.format(formatter);

        dates.add(dateDeparture1);
        dates.add(dateArrive1);
        dates.add(dateDeparture2);
        dates.add(dateArrive2);

        return dates;
    }


    @PostMapping("/api/add-flights/{flightsQty}")
    public ResponseDTO addFlight(@PathVariable int flightsQty) {
        try {
            for (int i = 0; i < flightsQty; i++) {
                Random random = new Random();
                boolean isTwoWay = Math.random() > 0.10;

                // TODO: get all planes' id and choose one
                List<Integer> planesId = jdbc.getPlanesId();

                int planeId = random.nextInt(planesId.size());

                int selectedPlane = planesId.get(planeId);

//                System.out.println(planesId + "\n" + planeId + "\n" + "nr: " + selectedPlane);

                // all airports' id
                List<Integer> flightsId = jdbc.getAirportsId();

                // TODO: get all flights' airport and choose one for departure
                int randFlightDeparture = random.nextInt(flightsId.size());
                int flightIdDeparture = flightsId.get(randFlightDeparture);

                // TODO: get all flights' airport and choose one for arrive
                int randFlightArrive;

                do {
                    randFlightArrive = random.nextInt(flightsId.size());
                } while (randFlightArrive == randFlightDeparture);

                int flightIdArrive = flightsId.get(randFlightArrive);

//                System.out.println(flightsId + "\n" + randFlightArrive + "\n" + "nr: " + flightIdArrive);

//                System.out.println(flightsId + "\n" + randFlightDeparture + "\n" + "nr: " + flightIdDeparture);

                // TODO: random Gate and Terminal
                int randGate1 = (int) Math.floor(Math.random() * 70) + 1;
                int randTerminal1 = (int) Math.floor(Math.random() * 5) + 1;

                int randGate2 = (int) Math.floor(Math.random() * 70) + 1;
                int randTerminal2 = (int) Math.floor(Math.random() * 5) + 1;

                // TODO: randomArriveTime and randomDepartureTime
                ArrayList<String> dates = getDates();

                insertFlight(selectedPlane, flightIdArrive, dates.get(1), flightIdDeparture, dates.get(0), randGate1, randTerminal1);

                if (isTwoWay) {
                    insertFlight(selectedPlane, flightIdDeparture, dates.get(3), flightIdArrive, dates.get(2), randGate2, randTerminal2);
                }

                System.out.println("\n");
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
