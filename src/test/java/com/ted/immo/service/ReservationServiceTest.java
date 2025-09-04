package com.ted.immo.service;

import com.ted.immo.dto.ReservationRequest;
import com.ted.immo.exception.PropertyNotFoundException;
import com.ted.immo.exception.UserNotFoundException;
import com.ted.immo.model.Property;
import com.ted.immo.model.Reservation;
import com.ted.immo.model.ReservationStatut;
import com.ted.immo.model.User;
import com.ted.immo.repository.PropertyRepository;
import com.ted.immo.repository.ReservationRepository;
import com.ted.immo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private ReservationService reservationService;

    private User customer;
    private Property property;
    private ReservationRequest reservationRequest;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new User();
        ReflectionTestUtils.setField(customer, "id", 1L);

        property = new Property();
        ReflectionTestUtils.setField(property, "id", 10L);

        reservationRequest = new ReservationRequest();
        reservationRequest.setCustomerId(1L);
        reservationRequest.setPropertyId(10L);
        reservationRequest.setMessage("Nouvelle réservation");
        reservationRequest.setStatus(ReservationStatut.CONFIRMED);

        // Init reservation entity
        reservation = new Reservation();
        ReflectionTestUtils.setField(reservation, "id", 100L);
        reservation.setMessage("Nouvelle réservation");
        reservation.setStatus(ReservationStatut.CONFIRMED);
        reservation.setCustomer(customer);
        reservation.setProperty(property);
    }

    @Test
    void should_create_reservation_successfully() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(propertyRepository.findById(10L)).thenReturn(Optional.of(property));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // when
        Reservation saved = reservationService.createReservation(reservationRequest);

        // then
        assertThat(saved.getId()).isEqualTo(100L);
        assertThat(saved.getMessage()).isEqualTo("Nouvelle réservation");
        assertThat(saved.getStatus()).isEqualTo(ReservationStatut.CONFIRMED);
        assertThat(saved.getCustomer().getId()).isEqualTo(1L);
        assertThat(saved.getProperty().getId()).isEqualTo(10L);

        verify(userRepository).findById(1L);
        verify(propertyRepository).findById(10L);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> reservationService.createReservation(reservationRequest));

        verify(userRepository).findById(1L);
        verifyNoInteractions(propertyRepository, reservationRepository);
    }

    @Test
    void should_throw_exception_when_property_not_found() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(propertyRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(PropertyNotFoundException.class,
                () -> reservationService.createReservation(reservationRequest));

        verify(userRepository).findById(1L);
        verify(propertyRepository).findById(10L);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    void should_get_reservation_by_id() {
        // given
        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));

        // when
        Optional<Reservation> found = reservationService.getReservation(100L);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(100L);

        verify(reservationRepository).findById(100L);
    }

    @Test
    void should_get_all_reservations() {
        // given
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        // when
        List<Reservation> reservations = reservationService.getReservations();

        // then
        assertThat(reservations.get(0).getId()).isEqualTo(100L);

        verify(reservationRepository).findAll();
    }
}