package com.example.aviationservice.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.IOException;
import java.io.Serializable;

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
    @JsonSerialize(using = LocationToLatLngSerializer.class)
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

    static class LocationToLatLngSerializer extends JsonSerializer<Geometry> {

        @Override
        public void serialize(Geometry value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("lat", value.getCoordinate().x);
            gen.writeNumberField("lon", value.getCoordinate().y);
            gen.writeEndObject();
        }
    }
}

