package com.example.aviationservice.init;

import com.example.aviationservice.domain.Airport;
import com.example.aviationservice.domain.AirportType;
import com.example.aviationservice.domain.Country;
import com.example.aviationservice.domain.Region;
import com.example.aviationservice.repositories.AirportRepository;
import com.example.aviationservice.repositories.CountryRepository;
import com.example.aviationservice.repositories.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
public class DataLoader {
    private final boolean loadData;
    private final String airportsUrl;
    private final String countriesUrl;
    private final String regionsUrl;
    private final AirportRepository airportRepository;
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public DataLoader(
            final AirportRepository airportRepository,
            final CountryRepository countryRepository,
            final RegionRepository regionRepository,
            @Value("${data.load}") final boolean loadData,
            @Value("${data.airports_url}") final String airportsUrl,
            @Value("${data.countries_url}") final String countriesUrl,
            @Value("${data.regions_url}") final String regionsUrl
    ) {
        this.airportRepository = airportRepository;
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.loadData = loadData;
        this.airportsUrl = airportsUrl;
        this.countriesUrl = countriesUrl;
        this.regionsUrl = regionsUrl;
        initialize();
    }

    public void initialize() {
        if (loadData) {
            try {
                loadRegions(new URL(regionsUrl).openStream());
                loadCountries(new URL(countriesUrl).openStream());
                loadAirports(new URL(airportsUrl).openStream());
            } catch (Exception e) {
                log.error("Error loading data", e);
            }
        }
    }

    public void loadCountries(InputStream input) throws Exception {
        try {
            String[] HEADERS = { "id","code","name","continent","wikipedia_link","keywords"};
            Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(reader);
            for (CSVRecord record : records) {
                int id = Integer.parseInt(record.get("id"));
                Optional<Country> optionalCountry = countryRepository.findById(id);
                Country country = optionalCountry.orElseGet(Country::new);

                country.setId(id);
                country.setCode(record.get("code"));
                country.setName(record.get("name"));
                country.setContinent(record.get("continent"));
                country.setWikipediaLink(record.get("wikipedia_link"));
                country.setKeywords(record.get("keywords"));
                countryRepository.save(country);
            }
        } catch (Exception e) {
            log.error("Could not load countries", e);
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

    public void loadRegions(InputStream input) throws Exception {
        try {
            String[] HEADERS = { "id","code","local_code","name","continent","iso_country","wikipedia_link"};
            Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(reader);
            for (CSVRecord record : records) {
                int id = Integer.parseInt(record.get("id"));
                Optional<Region> optionalRegion = regionRepository.findById(id);
                Region region = optionalRegion.orElseGet(Region::new);
                region.setId(id);
                region.setCode(record.get("code"));
                region.setLocalCode(record.get("local_code"));
                region.setName(record.get("name"));
                region.setContinent(record.get("continent"));
                region.setIsoCountry(record.get("iso_country"));
                region.setWikipediaLink(record.get("wikipedia_link"));
                regionRepository.save(region);
            }
        } catch (Exception e) {
            log.error("Could not load countries", e);
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

    public void loadAirports(InputStream input) throws Exception {
        try {
            String[] HEADERS = {"id", "ident", "type", "name", "latitude_deg", "longitude_deg", "elevation_ft", "continent", "iso_country", "iso_region", "municipality", "scheduled_service", "gps_code", "iata_code", "local_code", "home_link", "wikipedia_link", "keywords"};
            Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(reader);
            for (CSVRecord record : records) {
                int id = Integer.parseInt(record.get("id"));
                Optional<Airport> optionalAirport = airportRepository.findById(id);
                Airport airport = optionalAirport.orElseGet(Airport::new);
                airport.setId(id);
                airport.setType(AirportType.valueOf(record.get("type")));
                airport.setIdent(record.get("ident"));
                airport.setLocalCode(record.get("local_code"));
                airport.setName(record.get("name"));
                airport.setContinent(record.get("continent"));
                String countryCode = record.get("iso_country");
                if (countryCode != null && countryCode.length() > 0) {
                    Optional<Country> country = countryRepository.findByCode(countryCode);
                    country.ifPresent(airport::setCountry);
                }
                String regionCode = record.get("iso_region");
                if (regionCode != null && regionCode.length() > 0) {
                    Optional<Region> region = regionRepository.findByCode(regionCode);
                    region.ifPresent(airport::setRegion);
                }
                airport.setGpsCode(record.get("gps_code"));
                airport.setIataCode(record.get("iata_code"));
                airport.setLocalCode(record.get("local_code"));
                airport.setHomeLink(record.get("home_link"));
                airport.setWikipediaLink(record.get("wikipedia_link"));
                airport.setKeywords(record.get("keywords"));

                String latitude = record.get("latitude_deg");
                if (latitude != null && latitude.length() > 0) {
                    airport.setLatitude(BigDecimal.valueOf(Double.parseDouble(latitude)).setScale(5, RoundingMode.HALF_UP ));
                }
                String longitude = record.get("longitude_deg");
                if (longitude != null && longitude.length() > 0) {
                    airport.setLongitude(BigDecimal.valueOf(Double.parseDouble(longitude)).setScale(5, RoundingMode.HALF_UP ));
                }
                airportRepository.save(airport);
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
}
