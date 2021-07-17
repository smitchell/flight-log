package com.example.aviationservice.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Entity
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
    private Integer elevation;
    private String continent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
    private String municipality;
    private boolean scheduledService;
    private String gpsCode;
    private String iataCode;
    private String localCode;
    private String homeLink;
    private String wikipediaLink;
    private String keywords;
}

