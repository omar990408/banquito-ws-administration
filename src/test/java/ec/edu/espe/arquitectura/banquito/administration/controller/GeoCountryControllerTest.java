package ec.edu.espe.arquitectura.banquito.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoCountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

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
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteLogic() {
    }

    @Test
    void findByCountryCode() {
    }

    @Test
    void findCountries() {
    }
}