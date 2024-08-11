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
import static org.mockito.ArgumentMatchers.anyInt;
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

    @Test
    void createRouteOriginOrDestionation_NotFound() throws Exception {
        when(service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build())).thenThrow(BadRequestException.class);
        String json = "{ \"origin\":  1 , \"destination\": 2, \"cost\": 10 }";
        mockMvc.perform(
                post("/routes")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRoute_OK() throws Exception {
        when(service.create(CreateRouteDto.builder().origin(1).destination(2).cost(10).build())).thenReturn(Route.builder().id(1).build());
        String json = "{ \"origin\":  1 , \"destination\": 2, \"cost\": 10 }";
        mockMvc.perform(
                        post("/routes")
                                .contentType("application/json")
                                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetRoutes() throws Exception {
        mockMvc.perform(get("/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }


    @Test
    public void testEliminarRuta_Success() throws Exception {
        Integer routeId = 1;

        mockMvc.perform(delete("/routes/{id}", routeId))
                .andExpect(status().isOk())
                .andExpect(content().string(routeId.toString()));
    }

    @Test
    public void testEliminarRuta_NotFound() throws Exception {
        Integer routeId = 1;

        doThrow(new BadRequestException("Route not found")).when(service).deleteRoute(routeId);

        mockMvc.perform(delete("/routes/{id}", routeId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Route not found"));
    }


    @Test
    public void getOptimalRoute_Success() throws Exception {
        Station origen = Station.builder().name("A").id(1).build();
        Station destination = Station.builder().name("B").id(2).build();

        when(service.getOptimalRoute(anyInt(), anyInt())).thenReturn(List.of(origen, destination));

        mockMvc.perform(get("/routes/optimal")
                        .param("origin", "1")
                        .param("destiny", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetOptimalRoute_BadRequest() throws Exception {
        String errorMessage = "Origin Not Found";

        when(service.getOptimalRoute(anyInt(), anyInt())).thenThrow(new BadRequestException(errorMessage));

        mockMvc.perform(get("/routes/optimal")
                        .param("origin", "1")
                        .param("destiny", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void initDatabaseRoutes() throws Exception {
        doNothing().when(service).initDB();

        mockMvc.perform(
                        get("/routes/initdb")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Iniciado"));
    }
}
