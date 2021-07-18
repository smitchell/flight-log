package com.example.aviationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airports implements Serializable {
    private Bounds bounds;
    private List<Airport> airports;
}
