package com.example.aviationservice.init;

import com.example.aviationservice.domain.Airport;
import com.example.aviationservice.domain.AirportType;
import com.example.aviationservice.repositories.AirportRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DataLoader {
    private final boolean loadData;
    private final String airportsUrl;
    private final AirportRepository airportRepository;

    @Autowired
    public DataLoader(
            final AirportRepository airportRepository,
            @Value("${data.load}") final boolean loadData,
            @Value("${data.airports_url}") final String airportsUrl
    ) {
        this.airportRepository = airportRepository;
        this.loadData = loadData;
        this.airportsUrl = airportsUrl;
        initialize();
    }

    public void initialize() {
        if (loadData) {
            try {
                loadAirports(new URL(airportsUrl).openStream());
            } catch (Exception e) {
                log.error("Error loading data", e);
            }
        }
    }


    public void loadAirports(InputStream input) throws Exception {
        try {
            String[] HEADERS = {"id", "ident", "type", "name", "latitude_deg", "longitude_deg", "elevation_ft", "continent", "iso_country", "iso_region", "municipality", "scheduled_service", "gps_code", "iata_code", "local_code", "home_link", "wikipedia_link", "keywords"};
            Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(reader);
            List<Airport> airports = new ArrayList<>();
            for (CSVRecord record : records) {
                String countryCode = record.get("iso_country");
                AirportType type = AirportType.valueOf(record.get("type"));
                List<AirportType> ignoredTypes = new ArrayList<>();
                ignoredTypes.add(AirportType.balloonport);
                ignoredTypes.add(AirportType.closed);
                ignoredTypes.add(AirportType.seaplane_base);
                ignoredTypes.add(AirportType.heliport);
                if (!"US".equals(countryCode) || ignoredTypes.contains(type)) {
                    continue;
                }
                int id = Integer.parseInt(record.get("id"));
                Optional<Airport> optionalAirport = airportRepository.findById(id);
                Airport airport = optionalAirport.orElseGet(Airport::new);
                airport.setId(id);
                airport.setType(type);
                airport.setIdent(record.get("ident"));
                airport.setLocalCode(record.get("local_code"));
                airport.setName(record.get("name"));
                airport.setContinent(record.get("continent"));
                airport.setCountry(countryCode);
                airport.setRegion(record.get("iso_region"));
                String regionCode = record.get("iso_region");
                airport.setGpsCode(record.get("gps_code"));
                airport.setIataCode(record.get("iata_code"));
                airport.setLocalCode(record.get("local_code"));
                airport.setHomeLink(record.get("home_link"));
                airport.setWikipediaLink(record.get("wikipedia_link"));
                airport.setScheduledService("yes".equals(record.get("scheduled_service")));
                boolean hasLatitude = false;
                String latitude = record.get("latitude_deg");
                if (latitude != null && latitude.length() > 0) {
                    airport.setLatitude(BigDecimal.valueOf(Double.parseDouble(latitude)).setScale(5, RoundingMode.HALF_UP));
                    hasLatitude = true;
                }
                boolean hasLongitude = false;
                String longitude = record.get("longitude_deg");
                if (longitude != null && longitude.length() > 0) {
                    airport.setLongitude(BigDecimal.valueOf(Double.parseDouble(longitude)).setScale(5, RoundingMode.HALF_UP));
                    hasLongitude = true;
                }
                if (hasLatitude && hasLongitude) {
                    Geometry point = new GeometryFactory().createPoint(new Coordinate(Double.parseDouble(longitude), Double.parseDouble(latitude)));
                    airport.setLocation(point);
                }
                log.info("loadAirports() " + airport.getName());
                airports.add(airport);
                if (airports.size() > 500) {
                    saveAirports(airports);
                    airports = new ArrayList<>();
                }
            }
            if (airports.size() > 0) {
                saveAirports(airports);
            }

        } catch (Exception e) {
            log.error("Could not load airports", e);
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ioe) {
                    log.error("Could not close input stream");
                }
            }
            throw e;
        }
    }

    @Transactional
    public void saveAirports(List<Airport> airports) {
        airportRepository.saveAll(airports);
    }
}
