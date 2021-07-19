package com.example.aviationservice.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airports implements Serializable {
    @JsonSerialize(using = Airports.BoundsSerializer.class)
    private Bounds bounds;
    private List<Airport> data;

    // This class fixes format of doubls
    static class BoundsSerializer extends JsonSerializer<Bounds> {
        @Override
        public void serialize(Bounds value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("minLat", value.getMinLat());
            gen.writeNumberField("maxLat", value.getMaxLat());
            gen.writeNumberField("minLon", value.getMinLon());
            gen.writeNumberField("maxLon", value.getMaxLon());
            gen.writeEndObject();
        }
    }
}
