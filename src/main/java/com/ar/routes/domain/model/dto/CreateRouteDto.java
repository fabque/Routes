package com.ar.routes.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateRouteDto {
    private Integer origin;
    private Integer destination;
    private double cost;
}
