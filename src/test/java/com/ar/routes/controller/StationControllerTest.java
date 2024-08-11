package com.ar.routes.controller;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.service.StationService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(StationController.class)
@AutoConfigureMockMvc
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StationService service;

    /**
     * Test if Station already exists
     */
    @Test
    @Order(1)
    public void checkIfAlreadyExists() throws Exception {
        String json = "{\"name\": \"A\"}";

        when(service.add("A")).thenReturn(Station.builder().name("A").build());

        mockMvc.perform(
                        post("/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isConflict());
    }

    /**
     * Test if Station already exists
     */
    @Test
    @Order(2)
    public void checkCreated() throws Exception {
        String json = "{\"name\": \"A\"}";

        when(service.add("A")).thenReturn(Station.builder().id(1).name("A").build());

        mockMvc.perform(
                        post("/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());
    }

    /**
     * Test if Station already exists
     */
    @Test
    @Order(3)
    public void getStations() throws Exception {

        List<Station> stationList = new ArrayList<>();
        Station stationA = Station.builder().id(1).name("A").build();
        stationList.add(stationA);


        when(service.getAll()).thenReturn(stationList);

        mockMvc.perform(
                        get("/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testDelete_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(delete("/stations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(id.toString()));
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        Integer id = 1;

        doThrow(new BadRequestException("Route not found")).when(service).delete(id);

        mockMvc.perform(delete("/stations/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Route not found"));
    }
}
