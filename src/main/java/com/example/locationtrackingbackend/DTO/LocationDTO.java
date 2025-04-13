package com.example.locationtrackingbackend.DTO;


import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class LocationDTO {

    private double latitude;
    private double longitude;
    private OffsetDateTime timestamp;
    private Long userId;

}
