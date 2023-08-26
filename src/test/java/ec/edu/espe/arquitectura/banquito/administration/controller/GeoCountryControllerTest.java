package ec.edu.espe.arquitectura.banquito.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoStructureReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoCountryRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoStructure;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoCountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GeoCountryController.class)
class GeoCountryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoCountryService geoCountryService;

    @Autowired
    private ObjectMapper objectMapper;
        private static final String URL = "/api/v1/geoCountry";

    @Test
    void createGeoCountryTest() throws Exception {
        //given
        GeoCountryReq geoCountryReq = geoCountryExample();
        GeoCountry geoCountry = GeoCountry.builder()
                .countryCode(geoCountryReq.getCountryCode())
                .name(geoCountryReq.getName())
                .phoneCode(geoCountryReq.getPhoneCode())
                .geoStructures(geoCountryReq.getGeoStructures().stream().map(geoStructureReq -> GeoStructure.builder()
                        .levelCode(geoStructureReq.getLevelCode())
                        .name(geoStructureReq.getName())
                        .build()).collect(Collectors.toList()))
                .build();
        given(geoCountryService.create(any(GeoCountryReq.class)))
                .willReturn(geoCountry);
        //when
        ResultActions response = mockMvc.perform(post(URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(geoCountryReq)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    GeoCountry geoCountryResponse = objectMapper.readValue(result.getResponse().getContentAsString(), GeoCountry.class);
                    assertEquals(geoCountry.getCountryCode(), geoCountryResponse.getCountryCode());
                    assertEquals(geoCountry.getName(), geoCountryResponse.getName());
                    assertEquals(geoCountry.getPhoneCode(), geoCountryResponse.getPhoneCode());
                    assertEquals(geoCountry.getGeoStructures().size(), geoCountryResponse.getGeoStructures().size());
                });

    }
    @Test
    void createGeoCountryTestThrowException() throws Exception {
        //given
        GeoCountryReq geoCountryReq = geoCountryExample();

        given(geoCountryService.create(any(GeoCountryReq.class)))
                .willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(post(URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(geoCountryReq)));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void updateGeocountryTest() throws Exception {
        //given
        String code = "EC";
        GeoCountryReq geoCountryReq = GeoCountryReq.builder()
                .phoneCode("592")
                .build();
        GeoCountry geoCountryUpdated = GeoCountry.builder()
                .countryCode("EC")
                .name("Ecuador")
                .phoneCode(geoCountryReq.getPhoneCode())
                .geoStructures(new ArrayList<>())
                .build();
        given(geoCountryService.update(code, geoCountryReq)).willReturn(geoCountryUpdated);
        //when
        ResultActions response = mockMvc.perform(put(URL + "/update/" + code)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(geoCountryReq)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    GeoCountry geoCountryResponse = objectMapper.readValue(result.getResponse().getContentAsString(), GeoCountry.class);
                    assertEquals(geoCountryUpdated.getCountryCode(), geoCountryResponse.getCountryCode());
                    assertEquals(geoCountryUpdated.getPhoneCode(), geoCountryResponse.getPhoneCode());
                });
    }
    @Test
    void updateGeocountryTestThrowException() throws Exception {
        //given
        String code = "EC";
        GeoCountryReq geoCountryReq = GeoCountryReq.builder()
                .phoneCode("592")
                .build();
        given(geoCountryService.update(code, geoCountryReq)).willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(put(URL + "/update/" + code)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(geoCountryReq)));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteLogicGeoCountryTest() throws Exception {
        //given
        String code = "EC";
        GeoCountry geoCountryDeleted = GeoCountry.builder()
                .countryCode("EC")
                .name("Ecuador")
                .state("INA")
                .phoneCode("593")
                .geoStructures(new ArrayList<>())
                .build();
        given(geoCountryService.deleteLogic(code)).willReturn(geoCountryDeleted);
        //when
        ResultActions response = mockMvc.perform(put(URL + "/deleteLogic/" + code));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    GeoCountry geoCountryResponse = objectMapper.readValue(result.getResponse().getContentAsString(), GeoCountry.class);
                    assertEquals(geoCountryDeleted.getState(), geoCountryResponse.getState());
                });
    }

    @Test
    void deleteLogicGeoCountryTestThrowException() throws Exception {
        //given
        String code = "EC";
        given(geoCountryService.deleteLogic(code)).willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(put(URL + "/deleteLogic/" + code));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByCountryCodeGeoCountryTest() throws Exception {
        //given
        String code = "EC";
        GeoCountryRes geoCountry = GeoCountryRes.builder()
                .countryCode("EC")
                .name("Ecuador")
                .phoneCode("593")
                .geoStructures(new ArrayList<>())
                .build();
        given(geoCountryService.findByCountryCode(code)).willReturn(geoCountry);
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByCountryCode/" + code));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    GeoCountryRes geoCountryResponse = objectMapper.readValue(result.getResponse().getContentAsString(), GeoCountryRes.class);
                    assertEquals(geoCountry.getCountryCode(), geoCountryResponse.getCountryCode());
                    assertEquals(geoCountry.getName(), geoCountryResponse.getName());
                    assertEquals(geoCountry.getPhoneCode(), geoCountryResponse.getPhoneCode());
                    assertEquals(geoCountry.getGeoStructures().size(), geoCountryResponse.getGeoStructures().size());
                });
    }

    @Test
    void findByCountryCodeGeoCountryTestThrowException() throws Exception {
        //given
        String code = "EC";
        given(geoCountryService.findByCountryCode(code)).willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByCountryCode/" + code));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findCountriesGeoLocationTest() throws Exception {
        //given
        GeoCountryRes geoCountry1 = GeoCountryRes.builder()
                .countryCode("EC")
                .name("Ecuador")
                .phoneCode("593")
                .geoStructures(new ArrayList<>())
                .build();
        GeoCountryRes geoCountry2 = GeoCountryRes.builder()
                .countryCode("CO")
                .name("Colombia")
                .phoneCode("221")
                .geoStructures(new ArrayList<>())
                .build();
        List<GeoCountryRes> list = new ArrayList<>();
        list.add(geoCountry1);
        list.add(geoCountry2);
        given(geoCountryService.getCountries()).willReturn(list);
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findCountriesList"));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<GeoCountryRes> geoCountryResList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                    assertEquals(list.size(), geoCountryResList.size());
                });
    }
    @Test
    void findCountriesGeoLocationTestThrowException() throws Exception {
        //given
        given(geoCountryService.getCountries()).willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findCountriesList"));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    private GeoCountryReq geoCountryExample() {
        GeoCountryReq geoCountryReq = GeoCountryReq.builder()
                .countryCode("EC")
                .name("Ecuador")
                .phoneCode("593")
                .geoStructures(new ArrayList<>())
                .build();
        GeoStructureReq provincia = GeoStructureReq.builder()
                .levelCode(1)
                .name("Provincia")
                .build();
        geoCountryReq.getGeoStructures().add(provincia);
        GeoStructureReq canton = GeoStructureReq.builder()
                .levelCode(2)
                .name("Canton")
                .build();
        geoCountryReq.getGeoStructures().add(canton);
        GeoStructureReq parroquia = GeoStructureReq.builder()
                .levelCode(3)
                .name("Parroquia")
                .build();
        geoCountryReq.getGeoStructures().add(parroquia);
        return geoCountryReq;
    }
}