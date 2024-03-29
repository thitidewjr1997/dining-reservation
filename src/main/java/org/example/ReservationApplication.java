package org.example;

import org.apache.commons.lang3.ObjectUtils;
import org.example.model.dto.ReservationData;
import org.example.model.dto.ReservationPerDayDetails;
import org.example.model.dto.ReservationRequest;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationApplication {
    public static void main(String[] args) {
        Map<LocalDate, ReservationPerDayDetails> reservationMap = new HashMap<>();
        LocalDate transactionDate = LocalDate.now();
        var reservationDetails = new ArrayList<ReservationRequest>();
        /* ลูกค้าที่จองมาด้วยกันแต่มีจำนวนมากกว่า 4 คน จะต้องนั่งแยกโต๊ะกันไม่เบียดกัน */
        reservationDetails.add(ReservationRequest.builder()
                .reservationName("john")
                .reservationMobileNumber("0801234567")
                .reservationDate(LocalDate.of(2024, 3, 29))
                .reservationCheckInTime(LocalTime.of(13, 0))
                .reservationCheckOutTime(LocalTime.of(14, 0))
                .totalCustomer(7)
                .build()
        );
        /* ลูกค้าจองมา 2 กลุ่มกลุ่มละ 3 คนในเวลาเดียวกันต้องใช้โต๊ะทั้งหมด 2 ตัว */
        reservationDetails.add(ReservationRequest.builder()
                .reservationName("june")
                .reservationMobileNumber("0801234568")
                .reservationDate(LocalDate.of(2024, 3, 29))
                .reservationCheckInTime(LocalTime.of(18, 0))
                .reservationCheckOutTime(LocalTime.of(20, 0))
                .totalCustomer(3)
                .build()
        );
        reservationDetails.add(ReservationRequest.builder()
                .reservationName("job")
                .reservationMobileNumber("0801234569")
                .reservationDate(LocalDate.of(2024, 3, 29))
                .reservationCheckInTime(LocalTime.of(18, 0))
                .reservationCheckOutTime(LocalTime.of(21, 0))
                .totalCustomer(3)
                .build()
        );
        /* ลูกค้าจองมา 2 กลุ่ม แต่เป็นคนละเวลากัน ก็จะใช้โต๊ะตัวเดียว */
        reservationDetails.add(ReservationRequest.builder()
                .reservationName("jame")
                .reservationMobileNumber("0801234570")
                .reservationDate(LocalDate.of(2024, 3, 29))
                .reservationCheckInTime(LocalTime.of(14, 0))
                .reservationCheckOutTime(LocalTime.of(15, 0))
                .totalCustomer(4)
                .build()
        );
        reservationDetails.add(ReservationRequest.builder()
                .reservationName("jazz")
                .reservationMobileNumber("0801234571")
                .reservationDate(LocalDate.of(2024, 3, 29))
                .reservationCheckInTime(LocalTime.of(11, 0))
                .reservationCheckOutTime(LocalTime.of(13, 0))
                .totalCustomer(4)
                .build()
        );
        reserveTables(reservationMap, transactionDate, reservationDetails);
        System.out.println(reservationMap);
        Assertions.assertEquals(2, reservationMap.get(LocalDate.of(2024, 3, 29)).getTotalTable());
    }

    private static void reserveTables(Map<LocalDate, ReservationPerDayDetails> reservationMap, LocalDate transactionDate, List<ReservationRequest> reservationDetails) {
        System.out.printf("%s %s", "Transaction Date : ", transactionDate.toString());
        for (var request : reservationDetails) {
            var reservationPerDayDetails = reservationMap.get(request.getReservationDate());
            if (ObjectUtils.isEmpty(reservationPerDayDetails)) {
                List<ReservationData> reservationDataList = new ArrayList<>();
                var reservationData = ReservationData.builder()
                        .reservationName(request.getReservationName())
                        .reservationMobileNumber(request.getReservationMobileNumber())
                        .reservationCheckInTime(request.getReservationCheckInTime())
                        .reservationCheckOutTime(request.getReservationCheckOutTime())
                        .build();
                reservationDataList.add(reservationData);
                reservationPerDayDetails = ReservationPerDayDetails.builder()
                        .reservationDataList(reservationDataList)
                        .totalTable(calculateTotalTablePerReservation(request.getTotalCustomer()))
                        .build();
            } else {
                var reservationDataList = reservationPerDayDetails.getReservationDataList();
                var countTable = (int) reservationDataList.stream().filter(r -> (
                        (request.getReservationCheckInTime().isBefore(r.getReservationCheckInTime()) && request.getReservationCheckOutTime().isAfter(r.getReservationCheckInTime()) && request.getReservationCheckInTime().isBefore(r.getReservationCheckOutTime()))
                                || (request.getReservationCheckInTime().equals(r.getReservationCheckInTime()) && request.getReservationCheckOutTime().isAfter(r.getReservationCheckOutTime()))
                                || (request.getReservationCheckInTime().isAfter(r.getReservationCheckInTime()) && request.getReservationCheckInTime().isBefore(r.getReservationCheckOutTime()) && request.getReservationCheckOutTime().isAfter(r.getReservationCheckOutTime()))
                                || (request.getReservationCheckInTime().isBefore(r.getReservationCheckInTime()) && request.getReservationCheckOutTime().isAfter(r.getReservationCheckOutTime())
                                || (request.getReservationCheckInTime().equals(r.getReservationCheckInTime()) && request.getReservationCheckOutTime().equals(r.getReservationCheckOutTime())))
                )).count();

                var totalTable = calculateTotalTablePerReservation(request.getTotalCustomer());
                if (countTable > 0) {
                    var addTable = countTable + totalTable;
                    reservationPerDayDetails.setTotalTable(addTable);
                }

                var reservationData = ReservationData.builder()
                        .reservationName(request.getReservationName())
                        .reservationMobileNumber(request.getReservationMobileNumber())
                        .reservationCheckInTime(request.getReservationCheckInTime())
                        .reservationCheckOutTime(request.getReservationCheckOutTime())
                        .build();
                reservationDataList.add(reservationData);
                reservationPerDayDetails.setReservationDataList(reservationDataList);
            }
            reservationMap.put(request.getReservationDate(), reservationPerDayDetails);
        }
    }

    private static int calculateTotalTablePerReservation(int totalCustomer) {
        var totalTable = totalCustomer / 4;
        if (totalCustomer % 4 > 0) {
            totalTable++;
        }
        return totalTable;
    }
}