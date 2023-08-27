package ec.edu.espe.arquitectura.banquito.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoLocationReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoLocationRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoLocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GeoLocationController.class)
class GeoLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoLocationService geoLocationService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/api/v1/geoLocation";
    @Test
    void createGeoLocationTest() throws Exception {
        //given
        GeoLocationReq geoLocationReq = geoLocationExample();
        GeoLocation geoLocation = GeoLocation.builder()
                .uuid(UUID.randomUUID().toString())
                .countryCode(geoLocationReq.getCountryCode())
                .levelParentId(geoLocationReq.getLevelParentId())
                .levelParentName(geoLocationReq.getLevelParentName())
                .levelCode(geoLocationReq.getLevelCode())
                .levelName(geoLocationReq.getLevelName())
                .name(geoLocationReq.getName())
                .areaPhoneCode(geoLocationReq.getAreaPhoneCode())
                .zipCode(geoLocationReq.getZipCode())
                .build();
        given(geoLocationService.create(any(GeoLocationReq.class))).willReturn(geoLocation);
        //when
        ResultActions response = mockMvc.perform(post(URL + "/create")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(geoLocationReq)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    GeoLocation geoLocationResponse = objectMapper.readValue(json, GeoLocation.class);
                    assertEquals(geoLocationResponse.getCountryCode(), geoLocationReq.getCountryCode());
                    assertEquals(geoLocationResponse.getLevelParentId(), geoLocationReq.getLevelParentId());
                    assertEquals(geoLocationResponse.getLevelParentName(), geoLocationReq.getLevelParentName());
                    assertEquals(geoLocationResponse.getLevelCode(), geoLocationReq.getLevelCode());
                    assertEquals(geoLocationResponse.getLevelName(), geoLocationReq.getLevelName());
                    assertEquals(geoLocationResponse.getName(), geoLocationReq.getName());
                    assertEquals(geoLocationResponse.getAreaPhoneCode(), geoLocationReq.getAreaPhoneCode());
                    assertEquals(geoLocationResponse.getZipCode(), geoLocationReq.getZipCode());
                });
    }

    @Test
    void createGeoLocationTestThrowException() throws Exception {
        //given
        GeoLocationReq geoLocationReq = geoLocationExample();
        given(geoLocationService.create(any(GeoLocationReq.class))).willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(post(URL + "/create")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(geoLocationReq)));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGeoLocationTest() throws Exception {
        //given
        String id = UUID.randomUUID().toString();
        GeoLocationReq geoLocationReq = GeoLocationReq.builder().name("Quito Updated").build();
        GeoLocation geoLocation = GeoLocation.builder()
                .id(id)
                .countryCode("EC")
                .levelParentId("1")
                .levelParentName("Pichincha")
                .levelCode("2")
                .levelName("Canton")
                .name(geoLocationReq.getName())
                .areaPhoneCode("02")
                .zipCode("170150")
                .build();
        given(geoLocationService.update(id,geoLocationReq)).willReturn(geoLocation);
        //when
        ResultActions response = mockMvc.perform(put(URL + "/update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(geoLocationReq)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    GeoLocation geoLocationResponse = objectMapper.readValue(json, GeoLocation.class);
                    assertEquals(geoLocationResponse.getId(), id);
                    assertEquals(geoLocationResponse.getName(), geoLocationReq.getName());
                });
    }

    @Test
    void updateGeoLocationTestThrowException() throws Exception {
        //given
        String id = UUID.randomUUID().toString();
        GeoLocationReq geoLocationReq = GeoLocationReq.builder().name("Quito Updated").build();

        given(geoLocationService.update(id,geoLocationReq)).willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(put(URL + "/update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(geoLocationReq)));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByCountryCodeAndName() throws Exception {
        //given
        String countryCode = "EC";
        String name = "Quito";
        GeoLocationRes geoLocation = GeoLocationRes.builder()
                .uuid(UUID.randomUUID().toString())
                .countryCode(countryCode)
                .levelParentName("Pichincha")
                .levelName("Canton")
                .name(name)
                .areaPhoneCode("02")
                .zipCode("170150")
                .build();
        given(geoLocationService.findByCountryCodeAndName(countryCode, name)).willReturn(geoLocation);
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByCountryCodeAndName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("countryCode", countryCode)
                .param("name", name));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    GeoLocationRes geoLocationResponse = objectMapper.readValue(json, GeoLocationRes.class);
                    assertEquals(geoLocationResponse, geoLocation);
                });
    }

    @Test
    void findByCountryCodeAndParentName() throws Exception {
        //given
        String countryCode = "EC";
        String parentName = "Pichincha";
        GeoLocationRes geoLocation = GeoLocationRes.builder()
                .uuid(UUID.randomUUID().toString())
                .countryCode(countryCode)
                .levelParentName(parentName)
                .levelName("Canton")
                .name("Quito")
                .areaPhoneCode("02")
                .zipCode("170150")
                .build();
        GeoLocationRes geoLocation1 = GeoLocationRes.builder()
                .uuid(UUID.randomUUID().toString())
                .countryCode(countryCode)
                .levelParentName(parentName)
                .levelName("Canton")
                .name("Cumbaya")
                .areaPhoneCode("02")
                .zipCode("170250")
                .build();
        List<GeoLocationRes> geoLocations = new ArrayList<>();
        geoLocations.add(geoLocation);
        geoLocations.add(geoLocation1);
        given(geoLocationService.findByCountryCodeAndLevelParentName(countryCode, parentName)).willReturn(geoLocations);
        //when
        ResultActions response = mockMvc.perform(get(URL + "/CountryCodeAndParentName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("countryCode", countryCode)
                .param("parentName", parentName));
        //then
response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List geoLocationResponse = objectMapper.readValue(json, List.class);
                    assertEquals(geoLocationResponse.size(), geoLocations.size());
                });
    }

    @Test
    void findByUuidGeoLocationTest() throws Exception {
        //given
        String uuid = UUID.randomUUID().toString();
        GeoLocationRes geoLocation = GeoLocationRes.builder()
                .uuid(uuid)
                .countryCode("EC")
                .levelParentName("Pichincha")
                .levelName("Canton")
                .name("Quito")
                .areaPhoneCode("02")
                .zipCode("170150")
                .build();
        given(geoLocationService.findByUuid(uuid)).willReturn(geoLocation);
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByUuid/" + uuid)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    GeoLocationRes geoLocationResponse = objectMapper.readValue(json, GeoLocationRes.class);
                    assertEquals(geoLocationResponse, geoLocation);
                });
    }

    @Test
    void findByCountryCodeAndLevelCode() throws Exception {
        //given
        String countryCode = "EC";
        String levelCode = "2";
        GeoLocationRes geoLocation = GeoLocationRes.builder()
                .uuid(UUID.randomUUID().toString())
                .countryCode(countryCode)
                .levelParentName("Pichincha")
                .levelName("Canton")
                .name("Quito")
                .areaPhoneCode("02")
                .zipCode("170150")
                .build();
        GeoLocationRes geoLocation1 = GeoLocationRes.builder()
                .uuid(UUID.randomUUID().toString())
                .countryCode(countryCode)
                .levelParentName("Picncha")
                .levelName("Canton")
                .name("Cumbaya")
                .areaPhoneCode("02")
                .zipCode("170250")
                .build();
        List<GeoLocationRes> geoLocations = new ArrayList<>();
        geoLocations.add(geoLocation);
        geoLocations.add(geoLocation1);
        given(geoLocationService.findByCountryCodeAndLevelCode(countryCode, levelCode)).willReturn(geoLocations);
        //when
        ResultActions response = mockMvc.perform(get(URL + "/countryCode-levelCode/" + countryCode + "/" + levelCode)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List geoLocationResponse = objectMapper.readValue(json, List.class);
                    assertEquals(geoLocationResponse.size(), geoLocations.size());
                });
    }
    private GeoLocationReq geoLocationExample() {
        return GeoLocationReq.builder()
                .countryCode("EC")
                .levelParentId("1")
                .levelParentName("Pichincha")
                .levelCode("2")
                .levelName("Canton")
                .name("Quito")
                .areaPhoneCode("02")
                .zipCode("170150")
                .build();
    }
}