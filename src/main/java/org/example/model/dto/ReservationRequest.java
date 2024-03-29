package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReservationRequest {
    private String reservationName;
    private String reservationMobileNumber;
    private LocalDate reservationDate;
    private LocalTime reservationCheckInTime;
    private LocalTime reservationCheckOutTime;
    private Integer totalCustomer;
}
