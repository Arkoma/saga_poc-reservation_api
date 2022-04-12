package com.sagapoc.reservationservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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
    private int room;
    private Date hotelCheckinDate;
    private Date hotelCheckoutDate;
    private Long hotelReservationId;
    private String carMake;
    private String carModel;
    private String carAgency;
    private Date carRentalDate;
    private Date carReturnDate;
    private Long carReservationId;
    private String flightNumber;
    private String seatNumber;
    private Date flightDepartureDate;
    private Date flightReturnDate;
    private String flightReservationId;
    private StatusEnum status;


}
