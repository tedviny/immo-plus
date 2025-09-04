package com.ted.immo.service;


import com.ted.immo.dto.PropertyRequest;
import com.ted.immo.exception.PropertyNotFoundException;
import com.ted.immo.exception.UserNotFoundException;
import com.ted.immo.model.Property;
import com.ted.immo.model.PropertyStatut;
import com.ted.immo.model.User;
import com.ted.immo.model.vo.PropertyAddress;
import com.ted.immo.repository.PropertyRepository;
import com.ted.immo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PropertyService propertyService;

    private User owner;
    private PropertyRequest propertyRequest;
    private Property property;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        ReflectionTestUtils.setField(owner, "id", 1L);
        owner.setLastName("Dupont");

        PropertyAddress address = new PropertyAddress();
        address.setStreet("Rue de Paris");
        address.setCity("Villejuif");
        address.setPostalCode("94800");
        address.setCountry("France");

        propertyRequest = new PropertyRequest();
        propertyRequest.setTitle("Villa à vendre");
        propertyRequest.setDescription("Belle villa");
        propertyRequest.setPrice(BigDecimal.valueOf(500000));
        propertyRequest.setStatus(PropertyStatut.AVAILABLE);
        propertyRequest.setOwnerId(1L);
        propertyRequest.setAddress(address);

        property = new Property();
        ReflectionTestUtils.setField(owner, "id", 1L);
        property.setTitle("Villa à vendre");
        property.setDescription("Belle villa");
        property.setPrice(BigDecimal.valueOf(500000));
        property.setStatus(PropertyStatut.AVAILABLE);
        property.setOwner(owner);
        property.setAddress(address);
    }

    @Test
    void should_create_property_when_owner_exists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        Property saved = propertyService.createProperty(propertyRequest);

        assertNotNull(saved);
        assertEquals("Villa à vendre", saved.getTitle());
        verify(propertyRepository, times(1)).save(any(Property.class));
    }

    @Test
    void should_throw_exception_when_owner_not_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> propertyService.createProperty(propertyRequest));
        verify(propertyRepository, never()).save(any(Property.class));
    }

    @Test
    void should_return_property_when_found() {

        ReflectionTestUtils.setField(property, "id", 1L);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        Optional<Property> foundedProperty = propertyService.getProperty(1L);

        assertTrue(foundedProperty.isPresent());
        assertEquals(1L, foundedProperty.get().getId());
    }

    @Test
    void should_update_property_when_exists() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        PropertyRequest updateRequest = new PropertyRequest();
        updateRequest.setTitle("Nouvelle villa");
        updateRequest.setDescription("Description modifiée");
        updateRequest.setPrice(BigDecimal.valueOf(750000));
        updateRequest.setStatus(PropertyStatut.AVAILABLE);

        Property updated = propertyService.updateProperty(1L, updateRequest);

        assertEquals("Nouvelle villa", updated.getTitle());
        assertEquals(PropertyStatut.AVAILABLE, updated.getStatus());
        verify(propertyRepository, times(1)).save(property);
    }

    @Test
    void should_throw_exception_when_updating_nonexistent_property() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PropertyNotFoundException.class,
                () -> propertyService.updateProperty(1L, propertyRequest));
    }

    @Test
    void should_delete_property_when_exists() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        propertyService.deleteProperty(1L);

        verify(propertyRepository, times(1)).delete(property);
    }

    @Test
    void should_throw_exception_when_deleting_nonexistent_property() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PropertyNotFoundException.class,
                () -> propertyService.deleteProperty(1L));
        verify(propertyRepository, never()).delete(any(Property.class));
    }
}
