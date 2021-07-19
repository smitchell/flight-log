package com.example.aviationservice.controller;


import com.example.aviationservice.controllers.AirportController;
import com.example.aviationservice.domain.Airport;
import com.example.aviationservice.domain.Bounds;
import com.example.aviationservice.repositories.AirportRepository;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class AirportControllerTest {

    @Autowired
    private AirportRepository airportRepository;

    @Test
    public void testComputeAirportsBounds() {
        List<Airport> airports = new ArrayList<>();
        double lat = 1;
        double lon = -1;
        while (lat < 5) {
            Airport airport = new Airport();
            Geometry point = new GeometryFactory().createPoint(new Coordinate(lon--, lat++));
            airport.setLocation(point);
            airports.add(airport);
        }
        AirportController airportController = new AirportController(airportRepository);
        Bounds bounds = airportController.computeAirportsBounds(airports);
        assertEquals(4d, bounds.getMaxLat(), 0.0);
        assertEquals(1d, bounds.getMinLat(), 0.0);
        assertEquals(-4d, bounds.getMaxLon(), 0.0);
        assertEquals(-1d, bounds.getMinLon(), 0.0);
    }
}
