package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReservationData {
    private String reservationName;
    private String reservationMobileNumber;
    private LocalTime reservationCheckInTime;
    private LocalTime reservationCheckOutTime;
}
