package com.ar.routes.controller;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import com.ar.routes.domain.service.RouteService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc
public class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteService service;

    @Test
    void createRouteOriginOrDestionationNotFound() throws Exception {
        when(service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build())).thenThrow(BadRequestException.class);
        String json = "{ \"origin\":  1 , \"destination\": 2, \"cost\": 10 }";
        mockMvc.perform(
                post("/routes")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRouteOK() throws Exception {
        when(service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build())).thenReturn(Route.builder().id(1).build());
        String json = "{ \"origin\":  1 , \"destination\": 2, \"cost\": 10 }";
        mockMvc.perform(
                        post("/routes")
                                .contentType("application/json")
                                .content(json))
                .andExpect(status().isCreated());
    }
}
