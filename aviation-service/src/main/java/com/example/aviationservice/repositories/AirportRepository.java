package com.example.aviationservice.repositories;

import com.example.aviationservice.domain.Airport;
import com.example.aviationservice.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Integer> {
}
