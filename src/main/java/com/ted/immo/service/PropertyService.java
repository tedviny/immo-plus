package com.ted.immo.service;

import com.ted.immo.exception.PropertyNotFoundException;
import com.ted.immo.exception.UserNotFoundException;
import com.ted.immo.model.Property;
import com.ted.immo.dto.PropertyRequest;
import com.ted.immo.model.User;
import com.ted.immo.model.vo.PropertyAddress;
import com.ted.immo.repository.PropertyRepository;
import com.ted.immo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public PropertyService(PropertyRepository propertyRepository, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    public Property createProperty(PropertyRequest propertyRequest) {
        User owner = userRepository.findById(propertyRequest.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException(propertyRequest.getOwnerId()));

        Property property = new Property();
        property.setTitle(propertyRequest.getTitle());
        property.setDescription(propertyRequest.getDescription());
        property.setPrice(propertyRequest.getPrice());

        if (propertyRequest.getAddress() != null) {
            PropertyAddress propertyAddress = new PropertyAddress();
            propertyAddress.setStreet(propertyRequest.getAddress().getStreet());
            propertyAddress.setPostalCode(propertyRequest.getAddress().getPostalCode());
            propertyAddress.setCity(propertyRequest.getAddress().getCity());
            propertyAddress.setCountry(propertyRequest.getAddress().getCountry());
            property.setAddress(propertyAddress);
        }
        if (propertyRequest.getStatus() != null) {
            property.setStatus(propertyRequest.getStatus());
        }
        property.setOwner(owner);

        return propertyRepository.save(property);
    }

    public Optional<Property> getProperty(Long id) {
        return propertyRepository.findById(id);
    }

    public List<Property> getProperties() {
        return propertyRepository.findAll();
    }


    public Property updateProperty(Long propertyId, PropertyRequest updatedProperty) {

        Property existingProperty = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        existingProperty.setTitle(updatedProperty.getTitle());
        existingProperty.setDescription(updatedProperty.getDescription());

        if (updatedProperty.getAddress() != null) {
            existingProperty.setAddress(updatedProperty.getAddress());
        }

        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setStatus(updatedProperty.getStatus());

        return propertyRepository.save(existingProperty);
    }

    @Transactional
    public void deleteProperty(Long propertyId) {
        Property deletedProperty = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        propertyRepository.delete(deletedProperty);
    }

}
