package com.example.aviationservice.init;

import com.example.aviationservice.domain.Country;
import com.example.aviationservice.repositories.CountryRepository;
import com.example.aviationservice.repositories.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * data.load = false in application.yml, allowing Junit to initialize
 * DataLoader without loading any data so that individual loaders may be tested.
 */
@SpringBootTest
public class TestLoadCountries {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testLoadCountries() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:/countries.csv");
        dataLoader.loadCountries(resource.getInputStream());
        Optional<Country> optionalCountry = countryRepository.findById(302612);
        assertTrue(optionalCountry.isPresent());
        Country country = optionalCountry.get();
        assertEquals("ZW", country.getCode());
        assertEquals("Zimbabwe", country.getName());
        assertEquals("AF", country.getContinent());
        assertEquals("https://en.wikipedia.org/wiki/Zimbabwe", country.getWikipediaLink());
    }
}
