package com.example.lotycrud.Builders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetFlightBuilder {
    private int Id;
    private String model;
    private String arriveAirportName;
    private String arriveAirportSign;
    private String arriveDateTime;
    private String departureAirportName;
    private String departureAirportSign;
    private String departureDateTime;
    private int gate;
    private int terminal;
}
