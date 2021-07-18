package com.example.aviationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Coordinate;

@Data
@AllArgsConstructor
public class Circle {
    Coordinate[] cordinates;
}
