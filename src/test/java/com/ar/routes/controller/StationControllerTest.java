package com.ar.routes.controller;

import com.ar.routes.domain.model.Station;
import com.ar.routes.domain.model.dto.CreateEditStationDto;
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

    private static final Long id = 1L;
    /**
     * Test if Station already exists
     */
    @Test
    @Order(1)
    public void testCreate_checkIfAlreadyExists() throws Exception {
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
    public void checkCreated_OK() throws Exception {
        String json = "{\"name\": \"A\"}";

        when(service.add("A")).thenReturn(Station.builder().id(id).name("A").build());

        mockMvc.perform(
                        post("/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void testPutStation_Success() throws Exception {

        CreateEditStationDto editDto = new CreateEditStationDto("NewName");
        Station savedStation = new Station(id, "NewName");

        when(service.edit(id, editDto)).thenReturn(savedStation);

        mockMvc.perform(put("/stations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"NewName\"}"))
                .andExpect(status().isAccepted())
                .andExpect(content().string(id.toString()));
    }

    @Test
    public void testPutStation_BadRequest() throws Exception {
        CreateEditStationDto editDto = new CreateEditStationDto("ExistingName");

        when(service.edit(id, editDto)).thenThrow(new BadRequestException("Not Found"));

        mockMvc.perform(put("/stations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"ExistingName\"}"))
                .andExpect(status().isBadRequest());
    }


    /**
     * Test if Station already exists
     */
    @Test
    public void getStations() throws Exception {

        List<Station> stationList = new ArrayList<>();
        Station stationA = Station.builder().id(id).name("A").build();
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
        mockMvc.perform(delete("/stations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(id.toString()));
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        doThrow(new BadRequestException("Route not found")).when(service).delete(id);

        mockMvc.perform(delete("/stations/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Route not found"));
    }
}
