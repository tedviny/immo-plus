package com.ted.immo.controller;

import com.ted.immo.model.Property;
import com.ted.immo.dto.PropertyRequest;
import com.ted.immo.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    @PostMapping("/create")
    public ResponseEntity<Property> createProperty(@RequestBody PropertyRequest propertyRequest) {
        Property propertySaved = propertyService.createProperty(propertyRequest);
        return ResponseEntity.ok(propertySaved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getProperty(@PathVariable Long id) {
        return propertyService.getProperty(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Property>> getProperties() {
        List<Property> properties = propertyService.getProperties();
        return ResponseEntity.ok(properties);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody PropertyRequest property){

        Property updatedProperty = propertyService.updateProperty(id, property);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id){
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}
