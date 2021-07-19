package com.example.aviationservice.init;

import com.example.aviationservice.domain.Airport;
import com.example.aviationservice.domain.AirportType;
import com.example.aviationservice.repositories.AirportRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * data.load = false in application.yml, allowing Junit to initialize
 * DataLoader without loading any data so that individual loaders may be tested.
 */
@Slf4j
@SpringBootTest
@Sql(scripts = "classpath:/schema-h2.sql")
public class AirportLoaderTest {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private AirportRepository airportRepository;

    public AirportLoaderTest() throws ClassNotFoundException {
        Class.forName("org.h2gis.functions.factory.H2GISFunctions");
    }


    @Test
    @Transactional
    public void testFindAirportByAreaSuccess() throws Exception {
        dataLoader.loadAirports(resourceLoader.getResource("classpath:/airports.csv").getInputStream());
        GeometryFactory geomFactory = new GeometryFactory();

        Envelope env = new Envelope(
                new Coordinate(-80.518862, 25.564534),
                new Coordinate(-80.508013, 25.550651));

        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(env.getMinX(), env.getMinY());
        coords[1] = new Coordinate(env.getMinX(), env.getMaxY());
        coords[2] = new Coordinate(env.getMaxX(), env.getMaxY());
        coords[3] = new Coordinate(env.getMaxX(), env.getMinY());
        coords[4] = new Coordinate(env.getMinX(), env.getMinY());
        LinearRing shell = geomFactory.createLinearRing(coords);
        Polygon area = geomFactory.createPolygon(shell, null);
        area.setSRID(3857);

        List<Airport> airports = airportRepository.findWithinArea(area);
        assertNotNull(airports);
        assertEquals(1, airports.size());
        Airport match = airports.get(0);
        assertEquals("04FA", match.getIdent());
    }

    @Test
    @Transactional
    public void testFindAirportByAreaMiss() throws Exception {
        dataLoader.loadAirports(resourceLoader.getResource("classpath:/airports.csv").getInputStream());
        GeometryFactory geomFactory = new GeometryFactory();

        Envelope env = new Envelope(
                new Coordinate(-94.723415, 38.927415),
                new Coordinate(-94.686465, 38.912943));

        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(env.getMinX(), env.getMinY());
        coords[1] = new Coordinate(env.getMinX(), env.getMaxY());
        coords[2] = new Coordinate(env.getMaxX(), env.getMaxY());
        coords[3] = new Coordinate(env.getMaxX(), env.getMinY());
        coords[4] = new Coordinate(env.getMinX(), env.getMinY());
        LinearRing shell = geomFactory.createLinearRing(coords);
        Polygon area = geomFactory.createPolygon(shell, null);
        area.setSRID(3857);

        List<Airport> airports = airportRepository.findWithinArea(area);
        assertNotNull(airports);
        assertEquals(0, airports.size());
    }

    @Test
    @Transactional
    public void testFindAirportByRegionSuccess() throws Exception {
        dataLoader.loadAirports(resourceLoader.getResource("classpath:/airports.csv").getInputStream());
        List<Airport> airportList = airportRepository.findAllByRegionOrderByNameAsc("US-FL");
        assertNotNull(airportList);
        assertEquals(1, airportList.size());
        assertEquals("04FA", airportList.get(0).getIdent());
    }

    @Test
    @Transactional
    public void testFindAirportByRegionMiss() throws Exception {
        dataLoader.loadAirports(resourceLoader.getResource("classpath:/airports.csv").getInputStream());
        List<Airport> airportList = airportRepository.findAllByRegionOrderByNameAsc("US-KS");
        assertNotNull(airportList);
        assertEquals(0, airportList.size());
    }

    @Test
    @Transactional
    public void testFindAirportByRegionAndTypeSuccess() throws Exception {
        dataLoader.loadAirports(resourceLoader.getResource("classpath:/airports.csv").getInputStream());
        List<AirportType> types = new ArrayList<>();
        types.add(AirportType.small_airport);
        types.add(AirportType.medium_airport);
        types.add(AirportType.large_airport);
        List<Airport> airportList = airportRepository.findAllByRegionAndTypeInOrderByNameAsc("US-FL", types);
        assertNotNull(airportList);
        assertEquals(1, airportList.size());
        assertEquals("04FA", airportList.get(0).getIdent());
    }

    @Test
    @Transactional
    public void testFindAirportByRegionAndTypeMiss() throws Exception {
        dataLoader.loadAirports(resourceLoader.getResource("classpath:/airports.csv").getInputStream());
        List<AirportType> types = new ArrayList<>();
        types.add(AirportType.balloonport);
        types.add(AirportType.closed);
        types.add(AirportType.heliport);
        types.add(AirportType.seaplane_base);
        List<Airport> airportList = airportRepository.findAllByRegionAndTypeInOrderByNameAsc("US-FL", types);
        assertNotNull(airportList);
        assertEquals(0, airportList.size());
    }

    @Test
    @Transactional
    public void testLoadAndFindAirports() throws Exception {
        dataLoader.loadAirports(resourceLoader.getResource("classpath:/airports.csv").getInputStream());

        Optional<Airport> optionalAirport = airportRepository.findById(6760);
        assertTrue(optionalAirport.isPresent());
        Airport airport = optionalAirport.get();
        assertEquals("04FA", airport.getIdent());
        assertEquals(AirportType.small_airport, airport.getType());
        assertEquals("Richards Field", airport.getName());
        assertEquals("NA", airport.getContinent());
        assertEquals("US", airport.getCountry());
        assertEquals("US-FL", airport.getRegion());
        assertFalse(airport.isScheduledService());
        assertEquals("04FA", airport.getGpsCode());

        assertEquals(25.558700561523400D, airport.getLocation().getCoordinate().y);
        assertEquals(-80.51509857177730D, airport.getLocation().getCoordinate().x);
        assertNotNull(airport.getLocation());
    }
}
