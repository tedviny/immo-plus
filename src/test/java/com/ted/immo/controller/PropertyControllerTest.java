package com.ted.immo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ted.immo.SecurityConfig;
import com.ted.immo.dto.PropertyRequest;
import com.ted.immo.model.Property;
import com.ted.immo.model.PropertyStatut;
import com.ted.immo.model.vo.PropertyAddress;
import com.ted.immo.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PropertyController.class)
@Import(SecurityConfig.class)
public class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PropertyService propertyService;


    @Test
    void should_create_property() throws Exception {

        PropertyRequest propertyRequest = new PropertyRequest();
        PropertyAddress adresse = new PropertyAddress();
        Property property = new Property();

        adresse.setStreet("Avenue de Paris");
        adresse.setPostalCode("94800");
        adresse.setCity("Villejuif");
        adresse.setCountry("France");

        propertyRequest.setTitle("Villa à vendre");
        propertyRequest.setDescription("Très belle villa à vendre près de Paris");
        propertyRequest.setAddress(adresse);
        propertyRequest.setPrice(new BigDecimal(1000000));
        propertyRequest.setStatus(PropertyStatut.AVAILABLE);
        propertyRequest.setOwnerId(1L);

        property.setTitle(propertyRequest.getTitle());
        property.setDescription(propertyRequest.getDescription());
        property.setAddress(propertyRequest.getAddress());
        property.setPrice(propertyRequest.getPrice());
        property.setStatus(propertyRequest.getStatus());

        Field idField = Property.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(property, 1L);

        Mockito.when(propertyService.createProperty(any(PropertyRequest.class)))
                .thenReturn(property);

        mockMvc.perform(post("/api/v1/property/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propertyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Villa à vendre"))
                .andExpect(jsonPath("$.description").value("Très belle villa à vendre près de Paris"))
                .andExpect(jsonPath("$.address.street").value("Avenue de Paris"))
                .andExpect(jsonPath("$.address.city").value("Villejuif"))
                .andExpect(jsonPath("$.address.postalCode").value("94800"))
                .andExpect(jsonPath("$.address.country").value("France"))
                .andExpect(jsonPath("$.price").value(1000000))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void should_get_property_by_id() throws Exception {

        PropertyRequest propertyRequest = new PropertyRequest();
        PropertyAddress adresse = new PropertyAddress();
        Property property = new Property();

        adresse.setStreet("Avenue de Paris");
        adresse.setPostalCode("94800");
        adresse.setCity("Villejuif");
        adresse.setCountry("France");

        propertyRequest.setTitle("Villa à vendre");
        propertyRequest.setDescription("Très belle villa à vendre près de Paris");
        propertyRequest.setAddress(adresse);
        propertyRequest.setPrice(new BigDecimal(1000000));
        propertyRequest.setStatus(PropertyStatut.AVAILABLE);
        propertyRequest.setOwnerId(1L);

        property.setTitle(propertyRequest.getTitle());
        property.setDescription(propertyRequest.getDescription());
        property.setAddress(propertyRequest.getAddress());
        property.setPrice(propertyRequest.getPrice());
        property.setStatus(propertyRequest.getStatus());


        Field idField = Property.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(property, 1L);


        Mockito.when(propertyService.getProperty(1L)).thenReturn(Optional.of(property));

        mockMvc.perform(get("/api/v1/property/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Villa à vendre"))
                .andExpect(jsonPath("$.description").value("Très belle villa à vendre près de Paris"))
                .andExpect(jsonPath("$.address.street").value("Avenue de Paris"))
                .andExpect(jsonPath("$.address.city").value("Villejuif"))
                .andExpect(jsonPath("$.address.postalCode").value("94800"))
                .andExpect(jsonPath("$.address.country").value("France"))
                .andExpect(jsonPath("$.price").value(1000000))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void should_get_all_properties() throws Exception {

        Property property1 = new Property();
        PropertyAddress adresse1 = new PropertyAddress();
        PropertyAddress adresse2 = new PropertyAddress();

        adresse1.setStreet("Avenue de Paris");
        adresse1.setPostalCode("94800");
        adresse1.setCity("Villejuif");
        adresse1.setCountry("France");

        Field id1 = Property.class.getDeclaredField("id");
        id1.setAccessible(true);
        id1.set(property1, 1L);
        property1.setTitle("Titre 1");
        property1.setDescription("Très belle villa à vendre près de l'avenue de Paris");
        property1.setAddress(adresse1);
        property1.setPrice(new BigDecimal(1000000));
        property1.setStatus(PropertyStatut.AVAILABLE);


        Property property2 = new Property();

        adresse2.setStreet("Avenue de la République");
        adresse2.setPostalCode("94800");
        adresse2.setCity("Villejuif");
        adresse2.setCountry("France");
        Field id2 = Property.class.getDeclaredField("id");
        id2.setAccessible(true); id2.set(property2, 2L);
        property2.setTitle("Titre 2");
        property2.setDescription("Très belle villa à vendre près de l'avenue de la République");
        property2.setAddress(adresse2);
        property2.setPrice(new BigDecimal(1000000));
        property2.setStatus(PropertyStatut.AVAILABLE);


        List<Property> properties = Arrays.asList(property1, property2);
        Mockito.when(propertyService.getProperties()).thenReturn(properties);

        mockMvc.perform(get("/api/v1/property"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Titre 1"))
                .andExpect(jsonPath("$[0].description").value("Très belle villa à vendre près de l'avenue de Paris"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Titre 2"))
                .andExpect(jsonPath("$[1].description").value("Très belle villa à vendre près de l'avenue de la République"));

    }


    @Test
    void should_return_not_found_if_id_not_found() throws Exception {

        Mockito.when(propertyService.getProperty(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/property/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_update_property() throws Exception {

        Long propertyId = 1L;

        PropertyRequest propertyRequest = new PropertyRequest();

        PropertyAddress adresse = new PropertyAddress();

        adresse.setStreet("Avenue de Paris");
        adresse.setPostalCode("94800");
        adresse.setCity("Villejuif");
        adresse.setCountry("France");

        propertyRequest.setTitle("Villa à vendre");
        propertyRequest.setDescription("Très belle villa à vendre près de Paris");
        propertyRequest.setAddress(adresse);
        propertyRequest.setPrice(new BigDecimal(1200000));
        propertyRequest.setStatus(PropertyStatut.AVAILABLE);
        propertyRequest.setOwnerId(1L);

        Property updatedProperty = new Property();

        Field idField = Property.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(updatedProperty, 1L);

        updatedProperty.setTitle(propertyRequest.getTitle());
        updatedProperty.setDescription(propertyRequest.getDescription());
        updatedProperty.setAddress(propertyRequest.getAddress());
        updatedProperty.setPrice(propertyRequest.getPrice());
        updatedProperty.setStatus(propertyRequest.getStatus());

        Mockito.when(propertyService.updateProperty(eq(1L), any(PropertyRequest.class))).thenReturn(updatedProperty);

        mockMvc.perform(put("/api/v1/property/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Villa à vendre"))
                .andExpect(jsonPath("$.description").value("Très belle villa à vendre près de Paris"))
                .andExpect(jsonPath("$.address.street").value("Avenue de Paris"))
                .andExpect(jsonPath("$.address.city").value("Villejuif"))
                .andExpect(jsonPath("$.address.postalCode").value("94800"))
                .andExpect(jsonPath("$.address.country").value("France"))
                .andExpect(jsonPath("$.price").value(1200000))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        Mockito.verify(propertyService).updateProperty(eq(1L), Mockito.any(PropertyRequest.class));
    }



    @Test
    void should_delete_property() throws Exception {
        Mockito.doNothing().when(propertyService).deleteProperty(1L);

        mockMvc.perform(delete("/api/v1/property/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(propertyService).deleteProperty(1L);
    }



}
