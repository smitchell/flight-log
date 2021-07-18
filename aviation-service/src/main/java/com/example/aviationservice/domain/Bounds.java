package com.example.aviationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bounds implements Serializable {
    private double minLat;
    private double maxLat;
    private double minLon;
    private double maxLon;
}
