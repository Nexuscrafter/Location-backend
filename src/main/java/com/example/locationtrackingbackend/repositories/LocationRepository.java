package com.example.locationtrackingbackend.repositories;

import com.example.locationtrackingbackend.DTO.LocationDTO;
import com.example.locationtrackingbackend.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> getLocationsByUserId(Long user_id);
}
