package com.example.aviationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class State implements Serializable {
    private String code;
    private String name;
}
