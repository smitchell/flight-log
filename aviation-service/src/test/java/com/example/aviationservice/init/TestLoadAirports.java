package com.example.aviationservice.init;

import com.example.aviationservice.domain.Airport;
import com.example.aviationservice.domain.AirportType;
import com.example.aviationservice.domain.Country;
import com.example.aviationservice.domain.Region;
import com.example.aviationservice.repositories.AirportRepository;
import com.example.aviationservice.repositories.CountryRepository;
import com.example.aviationservice.repositories.RegionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * data.load = false in application.yml, allowing Junit to initialize
 * DataLoader without loading any data so that individual loaders may be tested.
 */
@SpringBootTest
public class TestLoadAirports {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private AirportRepository airportRepository;

    @Test
    @Transactional
    public void testLoadAirports() throws Exception {
        dataLoader.loadRegions(resourceLoader.getResource("classpath:/regions.csv").getInputStream());
        dataLoader.loadCountries(resourceLoader.getResource("classpath:/countries.csv").getInputStream());
        dataLoader.loadAirports(resourceLoader.getResource("classpath:/airports.csv").getInputStream());

        Optional<Airport> optionalAirport = airportRepository.findById(313629);
        assertTrue(optionalAirport.isPresent());
        Airport airport = optionalAirport.get();
        assertEquals("ZZZZ", airport.getIdent());
        assertEquals(AirportType.small_airport, airport.getType());
        assertEquals("Satsuma Iōjima Airport", airport.getName());
        assertEquals("AS", airport.getContinent());
        assertEquals("JP", airport.getCountry().getCode());
        assertEquals("JP-46", airport.getRegion().getCode());
        assertFalse( airport.isScheduledService());
        assertEquals("RJX7", airport.getGpsCode());
        assertEquals("http://wikimapia.org/6705190/Satsuma-Iwo-jima-Airport", airport.getWikipediaLink());
        assertEquals("SATSUMA,IWOJIMA,RJX7", airport.getKeywords());

        assertEquals(BigDecimal.valueOf(30.784722D).setScale(5, RoundingMode.HALF_UP), airport.getLatitude());
        assertEquals(BigDecimal.valueOf(130.270556).setScale(5, RoundingMode.HALF_UP), airport.getLongitude());

    }
}
