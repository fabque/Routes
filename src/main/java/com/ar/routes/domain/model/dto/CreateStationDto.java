package com.ar.routes.domain.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateStationDto {
    @NonNull
    private String name;
}
