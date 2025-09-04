package com.ted.immo.controller;

import com.ted.immo.dto.ReservationRequest;
import com.ted.immo.model.Property;
import com.ted.immo.model.Reservation;
import com.ted.immo.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest reservationRequest){
        Reservation reservationSaved = reservationService.createReservation(reservationRequest);
        return ResponseEntity.ok(reservationSaved);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations(){
        List<Reservation> reservations = reservationService.getReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        return reservationService.getReservation(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
