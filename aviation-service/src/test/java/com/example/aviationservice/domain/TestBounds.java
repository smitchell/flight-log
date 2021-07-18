package com.example.aviationservice.domain;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBounds {

    @Test
    public void testConstructor() {
        Bounds bounds = new Bounds(1, 2, 3, 4);
        assertEquals(1, bounds.getMinLat());
        assertEquals(2, bounds.getMaxLat());
        assertEquals(3, bounds.getMinLon());
        assertEquals(4, bounds.getMaxLon());
    }
}
