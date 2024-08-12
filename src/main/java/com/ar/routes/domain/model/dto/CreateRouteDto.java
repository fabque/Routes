package com.ar.routes.domain.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateRouteDto {
    @NonNull
    private Long origin;
    @NonNull
    private Long destination;
    private double cost;
}
