package ec.edu.espe.arquitectura.banquito.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.service.BankEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(BankEntityController.class)
class BankEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankEntityService underTest;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void findBankEntityByIntCode() throws Exception {
        //given
        String internationalCode = "123456789";
        BankEntityRes bankEntityRes = BankEntityRes.builder()
                .internationalCode(internationalCode)
                .name("Banco Test")
                .build();
        given(underTest.findBankEntityByInternationalCode(internationalCode))
                .willReturn(bankEntityRes);
        //when
        ResultActions response = mockMvc.perform(get("/api/v1/bankEntity/find/{internationalCode}", internationalCode));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(bankEntityRes)));
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findBranchByCode() {
    }

    @Test
    void findAllbyState() {
    }

    @Test
    void findAllbyLocation() {
    }

    @Test
    void findAllbyLocationAndState() {
    }
}