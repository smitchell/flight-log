package com.example.aviationservice.init;

import com.example.aviationservice.domain.Region;
import com.example.aviationservice.repositories.RegionRepository;
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
public class TestLoadRegions {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    public void testLoadRegions() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:/regions.csv");
        dataLoader.loadRegions(resource.getInputStream());
        Optional<Region> optionalRegion = regionRepository.findById(306319);
        assertTrue(optionalRegion.isPresent());
        Region region = optionalRegion.get();
        assertEquals("ZW-MW", region.getCode());
        assertEquals("MW", region.getLocalCode());
        assertEquals("Mashonaland West Province", region.getName());
        assertEquals("AF", region.getContinent());
        assertEquals("ZW", region.getIsoCountry());
        assertEquals("https://en.wikipedia.org/wiki/Mashonaland_West_Province", region.getWikipediaLink());
    }
}
