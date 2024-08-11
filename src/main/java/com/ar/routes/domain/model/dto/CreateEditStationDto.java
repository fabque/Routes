package com.ar.routes.domain.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateEditStationDto {
    @NonNull
    private String name;
}
