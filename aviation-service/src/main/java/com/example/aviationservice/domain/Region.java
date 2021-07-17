package com.example.aviationservice.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
public class Region implements Serializable {
    @Id
    private int id;
    @Column(name="code", length=10, nullable=false, unique=true)
    private String code;
    private String localCode;
    private String name;
    @Column(name="continent", length=2, nullable=false, unique=false)
    private String continent;
    @Column(name="iso_country", length=2, nullable=false, unique=false)
    private String isoCountry;
    private String  wikipediaLink;
}
