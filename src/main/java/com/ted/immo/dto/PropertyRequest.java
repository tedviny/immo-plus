package com.ted.immo.dto;

import com.ted.immo.model.PropertyStatut;
import com.ted.immo.model.vo.PropertyAddress;

import java.math.BigDecimal;

public class PropertyRequest {
    private String title;
    private String description;
    private PropertyAddress address;
    private BigDecimal price;
    private PropertyStatut status;
    private Long ownerId;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PropertyAddress getAddress() {
        return address;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PropertyStatut getStatus() {
        return status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(PropertyAddress address) {
        this.address = address;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(PropertyStatut status) {
        this.status = status;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
