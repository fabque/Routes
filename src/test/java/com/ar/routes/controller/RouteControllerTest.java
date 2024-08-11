package com.ar.routes.controller;

import com.ar.routes.domain.model.Route;
import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateRouteDto;
import com.ar.routes.domain.service.RouteService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc
public class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteService service;

    private static final Long routeId = 1L;

    @Test
    void createRouteOriginOrDestionation_NotFound() throws Exception {
        when(service.create(CreateRouteDto.builder().origin(1L).destination(2L).cost(10).build())).thenThrow(BadRequestException.class);
        String json = "{ \"origin\":  1 , \"destination\": 2, \"cost\": 10 }";
        mockMvc.perform(
                post("/paths")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRoute_OK() throws Exception {
        when(service.create(CreateRouteDto.builder().origin(1L).destination(2L).cost(10).build())).thenReturn(Route.builder().id(1L).build());
        String json = "{ \"origin\":  1 , \"destination\": 2, \"cost\": 10 }";
        mockMvc.perform(
                        post("/paths")
                                .contentType("application/json")
                                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetRoutes() throws Exception {
        mockMvc.perform(get("/paths"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }


    @Test
    public void testDelete_Success() throws Exception {
        mockMvc.perform(delete("/paths/{id}", routeId))
                .andExpect(status().isOk())
                .andExpect(content().string(routeId.toString()));
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        doThrow(new BadRequestException("Route not found")).when(service).deleteRoute(routeId);

        mockMvc.perform(delete("/paths/{id}", routeId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Route not found"));
    }


    @Test
    public void getOptimalRoute_Success() throws Exception {
        Station origen = Station.builder().name("A").id(1L).build();
        Station destination = Station.builder().name("B").id(2L).build();

        when(service.getOptimalRoute(anyLong(), anyLong())).thenReturn(List.of(origen, destination));

        mockMvc.perform(get("/paths/{origin}/{destiny}", 1L, 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetOptimalRoute_BadRequest() throws Exception {
        String errorMessage = "Origin Not Found";

        when(service.getOptimalRoute(anyLong(), anyLong())).thenThrow(new BadRequestException(errorMessage));

        mockMvc.perform(get("/paths/{origin}/{destiny}", 1L, 2L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void initDatabaseRoutes() throws Exception {
        doNothing().when(service).initDB();

        mockMvc.perform(
                        get("/paths/initdb")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Iniciado"));
    }
}
