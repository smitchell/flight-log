package com.example.aviationservice.domain;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "AIRPORTS", indexes = {
        @Index(name = "airport_region_index", columnList = "region"),
        @Index(name = "airport_region_type_index", columnList = "region,type")
})
/**
 * https://ourairports.com/data/
 */
public class Airport implements Serializable {
    @Id
    private Integer id;
    private String ident;
    private AirportType type;
    private String name;
    @Column(precision = 5, scale = 2)
    private BigDecimal latitude;
    @Column(precision = 5, scale = 2)
    private BigDecimal longitude;
    private Geometry location;
    private Integer elevation;
    private String continent;
    private String country;
    private String region;
    private String municipality;
    private boolean scheduledService;
    private String gpsCode;
    private String iataCode;
    private String localCode;
    private String homeLink;
    private String wikipediaLink;
    private String keywords;
}

