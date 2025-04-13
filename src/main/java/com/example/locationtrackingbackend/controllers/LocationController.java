package com.example.locationtrackingbackend.controllers;

import com.example.locationtrackingbackend.DTO.LocationDTO;
import com.example.locationtrackingbackend.models.Location;
import com.example.locationtrackingbackend.models.User;
import com.example.locationtrackingbackend.repositories.LocationRepository;
import com.example.locationtrackingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("api/location")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<LocationDTO> addLocation(@RequestBody LocationDTO locationDTO, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserProfileByJwt(jwt);
        Location location = new Location();
        location.setUser(user);
        location.setLongitude(locationDTO.getLongitude());
        location.setLatitude(locationDTO.getLatitude());
        location.setTimestamp(locationDTO.getTimestamp());
        locationRepository.save(location);
        locationDTO.setUserId(user.getId());
        return ResponseEntity.ok(locationDTO);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Location>> getLocationByUserId(@PathVariable Long userId) {

        List<Location> locations = locationRepository.getLocationsByUserId(userId);
        return ResponseEntity.ok(locations);
    }


}
