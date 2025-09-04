package com.ted.immo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ted.immo.SecurityConfig;
import com.ted.immo.dto.ReservationRequest;
import com.ted.immo.model.Reservation;
import com.ted.immo.model.ReservationStatut;
import com.ted.immo.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReservationController.class)
@Import(SecurityConfig.class)
public class ReservationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private ReservationService reservationService;

        @Test
        void should_create_reservation() throws Exception {

            ReservationRequest reservationRequest = new ReservationRequest();
            reservationRequest.setCustomerId(1L);
            reservationRequest.setPropertyId(1L);
            reservationRequest.setMessage("Nouvelle réservation");
            reservationRequest.setStatus(ReservationStatut.CONFIRMED);

            Reservation reservation = new Reservation();

            Field idField = Reservation.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(reservation, 1L);

            reservation.setMessage(reservationRequest.getMessage());
            reservation.setStatus(reservationRequest.getStatus());

            Mockito.when(reservationService.createReservation(any(ReservationRequest.class))).thenReturn(reservation);

            mockMvc.perform(post("/api/v1/reservation/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reservationRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.message").value("Nouvelle réservation"))
                    .andExpect(jsonPath("$.status").value("CONFIRMED"));
        }

        @Test
        void should_get_all_reservations() throws Exception {

            Reservation reservation1 = new Reservation();

            Field id1 = Reservation.class.getDeclaredField("id");
            id1.setAccessible(true);
            id1.set(reservation1, 1L);
            reservation1.setMessage("Réservation 1");

            Reservation reservation2 = new Reservation();
            Field id2 = Reservation.class.getDeclaredField("id");
            id2.setAccessible(true);
            id2.set(reservation2, 2L);
            reservation2.setMessage("Réservation 2");

            List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
            Mockito.when(reservationService.getReservations()).thenReturn(reservations);

            mockMvc.perform(get("/api/v1/reservation"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].message").value("Réservation 1"))
                    .andExpect(jsonPath("$[1].id").value(2L))
                    .andExpect(jsonPath("$[1].message").value("Réservation 2"));
        }

        @Test
        void should_get_reservation_by_id() throws Exception {

            Reservation reservation = new Reservation();
            Field idField = Reservation.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(reservation, 1L);
            reservation.setMessage("Réservation test");

            Mockito.when(reservationService.getReservation(1L)).thenReturn(Optional.of(reservation));

            mockMvc.perform(get("/api/v1/reservation/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.message").value("Réservation test"));
        }

        @Test
        void should_return_not_found_if_reservation_not_found() throws Exception {
            Mockito.when(reservationService.getReservation(2L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/reservation/42"))
                    .andExpect(status().isNotFound());
        }


}
