package com.example.aviationservice.repositories;

import com.example.aviationservice.domain.Airport;
import com.example.aviationservice.domain.AirportType;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Integer> {

    // https://gist.github.com/michael-simons/824ff17aebc1aa6fe4cf26a3fe795892
    @Query(value
            = "SELECT a FROM Airport a where intersects(a.location, :area) = true"
    )
    List<Airport> findWithinArea(final Geometry area);

    List<Airport> findAllByRegionOrderByNameAsc(String regionCode);

    List<Airport> findAllByRegionAndTypeInOrderByNameAsc(String regionCode, List<AirportType> types);

}
