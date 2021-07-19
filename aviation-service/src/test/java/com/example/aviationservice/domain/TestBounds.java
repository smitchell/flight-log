package com.example.aviationservice.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBounds {

    @Test
    public void testConstructor() {
        double first = 1D;
        double second = 2D;
        double third = 3D;
        double forth = 4D;
        Bounds bounds = new Bounds(
                first,
                second,
                third,
                forth);
        assertEquals(first, bounds.getMinLat());
        assertEquals(second, bounds.getMaxLat());
        assertEquals(third, bounds.getMinLon());
        assertEquals(forth, bounds.getMaxLon());
    }
}
