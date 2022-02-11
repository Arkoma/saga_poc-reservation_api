package com.sagapoc.reservationservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String customerName;
    private String hotelName;
    private Long hotelId;
    private String carMake;
    private String carModel;
    private Long carId;
    private String flightNumber;
    private String flightId;
    private StatusEnum status;


}
