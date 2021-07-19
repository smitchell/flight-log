package com.example.aviationservice.controllers;

import com.example.aviationservice.domain.*;
import com.example.aviationservice.repositories.AirportRepository;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AirportController {
    private final List<State> states;
    private final List<AirportType> airportTypes;
    private final AirportRepository airportRepository;

    @Autowired
    public AirportController(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
        this.states = new ArrayList<>();
        this.airportTypes = new ArrayList<>();
        loadStates();
        loadAirportTypes();
    }


    @GetMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<State> getStates() {
        return states;
    }

    @GetMapping(value = "/airport-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AirportType> getAirportTypes() {
        return airportTypes;
    }

    @GetMapping(value = "/airport/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Airport getAirports(@PathVariable int id) {
        Optional<Airport> optional = airportRepository.findById(id);
        return optional.orElse(null);
    }

    @GetMapping(value = "/airports-by-region", produces = MediaType.APPLICATION_JSON_VALUE)
    public Airports findAirportsByRegion(@RequestParam String region) {
        List<Airport> matches = airportRepository.findAllByRegionOrderByNameAsc(region);
        return createAirports(matches);
    }

    @GetMapping(value = "/airports-by-region-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Airports findAirportsByRegion(@RequestParam String region, @RequestParam List<String> types) {
        List<AirportType> typesList = new ArrayList<>();
        for (String type : types) {
            typesList.add(AirportType.valueOf(type));
        }
        List<Airport> matches = airportRepository.findAllByRegionAndTypeInOrderByNameAsc(region, typesList);
        return createAirports(matches);
    }

    @GetMapping(value = "/airports-by-radius", produces = MediaType.APPLICATION_JSON_VALUE)
    public Airports findAirportsByRadius(@RequestParam String lat, @RequestParam String lon, @RequestParam String radius_mi) {
        List<Airport> matches = airportRepository.findWithinArea(createCircle(Double.parseDouble(lon), Double.parseDouble(lat), Double.parseDouble(radius_mi)));
        return createAirports(matches);
    }

    private static Polygon createCircle(double x, double y, final double miles) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(64);
        shapeFactory.setCentre(new Coordinate(x, y));

        double RADIUS = miles/68.7;
        shapeFactory.setSize(RADIUS * 2);

        Polygon circlePolygon = shapeFactory.createCircle();
        circlePolygon.setSRID(4326);

        return circlePolygon;
    }

    private Airports createAirports(List<Airport> airportList) {
        if (airportList.isEmpty()) {
            return new Airports(new Bounds(), airportList);
        }

        return new Airports(computeAirportsBounds(airportList), airportList);
    }

    public Bounds computeAirportsBounds(List<Airport> airportList) {
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = Double.MIN_VALUE;
        for (Airport airport : airportList) {
            Coordinate coord = airport.getLocation().getCoordinate();
            double lat = Math.abs(coord.y);
            double lon = Math.abs(coord.x);
            if (Math.abs(minLat) > lat) {
                minLat = coord.y;
            }
            if (Math.abs(maxLat) < lat) {
                maxLat = coord.y;
            }
            if (Math.abs(minLon) > lon) {
                minLon = coord.x;
            }
            if (Math.abs(maxLon) < lon) {
                maxLon = coord.x;
            }
        }
        return new Bounds(minLat, maxLat, minLon, maxLon);
    }


    public void loadStates() {
        states.add(new State("US-AL", "Alabama"));
        states.add(new State("US-AK", "Alaska"));
        states.add(new State("US-AZ", "Arizona"));
        states.add(new State("US-AR", "Arkansas"));
        states.add(new State("US-CA", "California"));
        states.add(new State("US-CO", "Colorado"));
        states.add(new State("US-CT", "Connecticut"));
        states.add(new State("US-DE", "Delaware"));
        states.add(new State("US-FL", "Florida"));
        states.add(new State("US-GA", "Georgia"));
        states.add(new State("US-HI", "Hawaii"));
        states.add(new State("US-ID", "Idaho"));
        states.add(new State("US-IL", "Illinois"));
        states.add(new State("US-IN", "Indiana"));
        states.add(new State("US-IA", "Iowa"));
        states.add(new State("US-KS", "Kansas"));
        states.add(new State("US-KY", "Kentucky"));
        states.add(new State("US-LA", "Louisiana"));
        states.add(new State("US-ME", "Maine"));
        states.add(new State("US-MD", "Maryland"));
        states.add(new State("US-MA", "Massachusetts"));
        states.add(new State("US-MI", "Michigan"));
        states.add(new State("US-MN", "Minnesota"));
        states.add(new State("US-MS", "Mississippi"));
        states.add(new State("US-MO", "Missouri"));
        states.add(new State("US-MT", "Montana"));
        states.add(new State("US-NE", "Nebraska"));
        states.add(new State("US-NV", "Nevada"));
        states.add(new State("US-NH", "New Hampshire"));
        states.add(new State("US-NJ", "New Jersey"));
        states.add(new State("US-NM", "New Mexico"));
        states.add(new State("US-NY", "New York"));
        states.add(new State("US-NC", "North Carolina"));
        states.add(new State("US-ND", "North Dakota"));
        states.add(new State("US-OH", "Ohio"));
        states.add(new State("US-OK", "Oklahoma"));
        states.add(new State("US-OR", "Oregon"));
        states.add(new State("US-PA", "Pennsylvania"));
        states.add(new State("US-RI", "Rhode Island"));
        states.add(new State("US-SC", "South Carolina"));
        states.add(new State("US-SD", "South Dakota"));
        states.add(new State("US-TN", "Tennessee"));
        states.add(new State("US-TX", "Texas"));
        states.add(new State("US-UT", "Utah"));
        states.add(new State("US-VT", "Vermont"));
        states.add(new State("US-VA", "Virginia"));
        states.add(new State("US-WA", "Washington"));
        states.add(new State("US-WV", "West Virginia"));
        states.add(new State("US-WI", "Wisconsin"));
        states.add(new State("US-WY", "Wyoming"));
        states.add(new State("US-DC", "District of Columbia"));
    }

    private void loadAirportTypes() {
        airportTypes.add(AirportType.small_airport);
        airportTypes.add(AirportType.medium_airport);
        airportTypes.add(AirportType.large_airport);
    }
}
