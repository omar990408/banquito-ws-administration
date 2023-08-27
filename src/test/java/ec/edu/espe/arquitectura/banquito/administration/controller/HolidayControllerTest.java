package ec.edu.espe.arquitectura.banquito.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.HolidayReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.HolidayRes;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import ec.edu.espe.arquitectura.banquito.administration.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HolidayController.class)
class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HolidayService holidayService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/api/v1/holiday";

    @Test
    void createHolidayTest() throws Exception {
        //given
        HolidayReq holidayReq = getHolidayReq();
        Holiday holiday = Holiday.builder()
                .uuid(UUID.randomUUID().toString())
                .holidayDate(holidayReq.getHolidayDate())
                .countryCode(holidayReq.getCountryCode())
                .geoLocationId(holidayReq.getGeoLocationId())
                .name(holidayReq.getName())
                .type(holidayReq.getType())
                .state("ACT")
                .build();
        given(holidayService.create(holidayReq)).willReturn(holiday);
        //when
        ResultActions response = mockMvc.perform(post(URL + "/create")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(holidayReq)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Holiday holidayResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Holiday.class);
                    assertEquals(holiday.getUuid(), holidayResponse.getUuid());
                    assertEquals(holiday.getHolidayDate(), holidayResponse.getHolidayDate());
                    assertEquals(holiday.getCountryCode(), holidayResponse.getCountryCode());
                    assertEquals(holiday.getGeoLocationId(), holidayResponse.getGeoLocationId());
                    assertEquals(holiday.getName(), holidayResponse.getName());
                    assertEquals(holiday.getType(), holidayResponse.getType());
                    assertEquals(holiday.getState(), holidayResponse.getState());
                });
    }

    @Test
    void createHolidayTestThrowException() throws Exception {
        //given
        HolidayReq holidayReq = getHolidayReq();
        given(holidayService.create(holidayReq)).willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(post(URL + "/create")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(holidayReq)));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void updateHolidayTest() throws Exception {
        //given
        String uuid = UUID.randomUUID().toString();
        HolidayReq holidayReq = HolidayReq.builder().name("Feriado Test Updated").build();
        Holiday holidayUpdated = Holiday.builder()
                .uuid(uuid)
                .holidayDate(new Date())
                .countryCode("EC")
                .geoLocationId("aaa-bbb-ccc")
                .name(holidayReq.getName())
                .type("Nacional")
                .state("ACT")
                .build();
        given(holidayService.update(uuid, holidayReq)).willReturn(holidayUpdated);
        //when
        ResultActions response = mockMvc.perform(put(URL + "/update/" + uuid)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(holidayReq)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Holiday holidayResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Holiday.class);
                    assertEquals(holidayUpdated.getName(), holidayResponse.getName());
                });
    }
    @Test
    void updateHolidayTestThrowException () throws Exception {
        //given
        String uuid = UUID.randomUUID().toString();
        HolidayReq holidayReq = HolidayReq.builder().name("Feriado Test Updated").build();

        given(holidayService.update(uuid, holidayReq)).willThrow(new RuntimeException());
        //when
        ResultActions response = mockMvc.perform(put(URL + "/update/" + uuid)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(holidayReq)));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteHolidayTest() throws Exception {
        //given
        String uuid = UUID.randomUUID().toString();
        Holiday holiday = Holiday.builder()
                .uuid(uuid)
                .holidayDate(new Date())
                .countryCode("EC")
                .geoLocationId("aaa-bbb-ccc")
                .name("Feriado Test")
                .type("Nacional")
                .state("INA")
                .build();
        given(holidayService.deleteLogic(uuid)).willReturn(holiday);
        //when
        ResultActions response = mockMvc.perform(put(URL + "/delete/" + uuid)
                .contentType("application/json"));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Holiday holidayResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Holiday.class);
                    assertEquals("INA", holidayResponse.getState());
                });
    }
    @Test
    void findByCountryCode() throws Exception {
        //given
        String countryCode = "EC";
        HolidayRes holidayRes = HolidayRes.builder()
                .uuid(UUID.randomUUID().toString())
                .holidayDate(new Date())
                .countryCode("EC")
                .countryName("Ecuador")
                .geoLocationId("aaa-bbb-ccc")
                .geoLocationName("Quito")
                .name("Feriado Test")
                .type("Nacional")
                .build();
        HolidayRes holidayRes2 = HolidayRes.builder()
                .uuid(UUID.randomUUID().toString())
                .holidayDate(new Date())
                .countryCode("EC")
                .countryName("Ecuador")
                .geoLocationId("aaa-bbb-ccc")
                .geoLocationName("Cuenca")
                .name("Feriado Test 2")
                .type("Nacional")
                .build();
        given(holidayService.findbyCountryCode("EC")).willReturn(List.of(holidayRes, holidayRes2));
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByCountryCode/" + countryCode)
                .contentType("application/json"));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List holidayResList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                    assertEquals(2, holidayResList.size());
                });
    }

    @Test
    void findByGeoLocationId() throws Exception {
        //given
        String geoLocationId = "aaa-bbb-ccc";
        HolidayRes holidayRes = HolidayRes.builder()
                .uuid(UUID.randomUUID().toString())
                .holidayDate(new Date())
                .countryCode("EC")
                .countryName("Ecuador")
                .geoLocationId("aaa-bbb-ccc")
                .geoLocationName("Quito")
                .name("Feriado Test")
                .type("Nacional")
                .build();
        HolidayRes holidayRes2 = HolidayRes.builder()
                .uuid(UUID.randomUUID().toString())
                .holidayDate(new Date())
                .countryCode("EC")
                .countryName("Ecuador")
                .geoLocationId("aaa-bbb-ccc")
                .geoLocationName("Quito")
                .name("Feriado Test 2")
                .type("Nacional")
                .build();
        given(holidayService.findByLocationId(geoLocationId)).willReturn(List.of(holidayRes, holidayRes2));
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByGeoLocationId/" + geoLocationId)
                .contentType("application/json"));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List holidayResList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                    assertEquals(2, holidayResList.size());
                });
    }

    @Test
    void generateHolidaysTest() throws Exception {
        //given
        String id = "aaa-bbb-ccc";
        int year = 2023;
        int month = 8;
        boolean saturday = true;
        boolean sunday = false;
        List<Holiday> holidays = List.of(
                Holiday.builder()
                        .uuid(UUID.randomUUID().toString())
                        .holidayDate(new Date())
                        .countryCode("EC")
                        .geoLocationId("aaa-bbb-ccc")
                        .name("Domingo Fin De semana")
                        .type("Nacional")
                        .state("ACT")
                        .build(),
                Holiday.builder()
                        .uuid(UUID.randomUUID().toString())
                        .holidayDate(new Date())
                        .countryCode("EC")
                        .geoLocationId("aaa-bbb-ccc")
                        .name("Domingo Fin De semana 2")
                        .type("Nacional")
                        .state("ACT")
                        .build()
        );
        given(holidayService.generateHolidaysWeekendsCountries(year, month, saturday, sunday, id)).willReturn(holidays);
        //when
        ResultActions response = mockMvc.perform(post(URL + "/holidayCountry/generate-weekends/" + id)
                .contentType("application/json")
                .param("year", Integer.toString(year))
                .param("month", Integer.toString(month))
                .param("saturday", Boolean.toString(saturday))
                .param("sunday", Boolean.toString(sunday)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List holidayResList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
                    assertEquals(2, holidayResList.size());
                });
    }

    @Test
    void findByUuid() throws Exception {
        //given
        String uuid = UUID.randomUUID().toString();
        Holiday holiday = Holiday.builder()
                .uuid(uuid)
                .holidayDate(new Date())
                .countryCode("EC")
                .geoLocationId("aaa-bbb-ccc")
                .name("Feriado Test")
                .type("Nacional")
                .state("INA")
                .build();
        given(holidayService.findByUuid(uuid)).willReturn(holiday);
        //when
        ResultActions response = mockMvc.perform(get(URL + "/findByUuid/" + uuid)
                .contentType("application/json"));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Holiday holidayResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Holiday.class);
                    assertEquals(holidayResponse, holiday);
                });
    }
    private HolidayReq getHolidayReq() {
        return HolidayReq.builder()
                .holidayDate(new Date())
                .countryCode("EC")
                .geoLocationId("aaa-bbb-ccc")
                .name("Feriado Test")
                .type("Nacional")
                .build();
    }
}