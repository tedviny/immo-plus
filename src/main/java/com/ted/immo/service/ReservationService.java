package com.ted.immo.service;

import com.ted.immo.dto.ReservationRequest;
import com.ted.immo.exception.PropertyNotFoundException;
import com.ted.immo.exception.UserNotFoundException;
import com.ted.immo.model.Property;
import com.ted.immo.model.Reservation;
import com.ted.immo.model.User;
import com.ted.immo.repository.PropertyRepository;
import com.ted.immo.repository.ReservationRepository;
import com.ted.immo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final PropertyRepository propertyRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, PropertyRepository propertyRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    public Reservation createReservation(ReservationRequest reservationRequest) {


        User user = userRepository.findById(reservationRequest.getCustomerId())
                .orElseThrow(() -> new UserNotFoundException(reservationRequest.getCustomerId()));

        Property property = propertyRepository.findById(reservationRequest.getPropertyId())
                .orElseThrow(() -> new PropertyNotFoundException(reservationRequest.getPropertyId()));


        Reservation reservation = new Reservation();

        reservation.setMessage(reservationRequest.getMessage());
        reservation.setStatus(reservationRequest.getStatus());

        reservation.setCustomer(user);
        reservation.setProperty(property);

        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> getReservation(Long id){
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }
}
