package com.ted.immo.dto;

import com.ted.immo.model.ReservationStatut;

public class ReservationRequest {

    private String message;
    private ReservationStatut status;

    private Long customerId;

    private Long propertyId;

    public String getMessage() {
        return message;
    }

    public ReservationStatut getStatus() {
        return status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(ReservationStatut status) {
        this.status = status;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}
