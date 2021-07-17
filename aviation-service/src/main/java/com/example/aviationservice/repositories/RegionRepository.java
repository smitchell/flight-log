package com.example.aviationservice.repositories;

import com.example.aviationservice.domain.Country;
import com.example.aviationservice.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByCode(String code);
}
