package com.ar.routes.controller;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.service.StationService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
