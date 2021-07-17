package com.example.aviationservice.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
/**
 * https://ourairports.com/data/contries.csv/
 */
public class Country implements Serializable {
    @Id
    private int id;
    @Column(name="code", length=2, nullable=false, unique=true)
    private String code;
    @Column(name="name", length=100, nullable=false, unique=true)
    private String name;
    @Column(name="continent", length=2, nullable=false, unique=false)
    private String continent;
    private String wikipediaLink;
    private String keywords;
}
