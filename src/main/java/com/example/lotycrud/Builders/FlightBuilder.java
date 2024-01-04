package com.example.lotycrud.Builders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightBuilder {
    private int PlaneId;
    private int Arrive;
    private String ArriveDateTime;
    private int Departure;
    private String DepartureDateTime;
    private int Gate;
    private int Terminal;
}
