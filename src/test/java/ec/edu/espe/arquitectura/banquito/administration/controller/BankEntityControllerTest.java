package ec.edu.espe.arquitectura.banquito.administration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BranchRes;
import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import ec.edu.espe.arquitectura.banquito.administration.service.BankEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BankEntityController.class)
class BankEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankEntityService bankEntityService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/api/v1/bankEntity";
    @Test
    void findBankEntityByIntCodeTest() throws Exception {
        //given
        String internationalCode = "123456789";
        BankEntityRes bankEntityRes = BankEntityRes.builder()
                .internationalCode(internationalCode)
                .name("Banco Test")
                .build();
        given(bankEntityService.findBankEntityByInternationalCode(internationalCode))
                .willReturn(bankEntityRes);
        //when
        ResultActions response = mockMvc.perform(get(URL+"/find/{internationalCode}", internationalCode));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(bankEntityRes)));
    }

    @Test
    void createBranchTestThrowException() throws Exception {
        //given
        BranchReq branchReq = BranchReq.builder()
                .code("CODE-TEST-"+ UUID.randomUUID())
                .bankEntityId("64bc71e6e9a5d42c0b3029d2")
                .locationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Branch Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
        given(bankEntityService.createBranch(any(BranchReq.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // when
        ResultActions response = mockMvc.perform(post(URL+"/addBranch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branchReq)));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void createBranchTest() throws Exception {
        //given
        BranchReq branchReq = BranchReq.builder()
                .code("CODE-TEST-"+ UUID.randomUUID())
                .bankEntityId("64bc71e6e9a5d42c0b3029d2")
                .locationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Branch Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
        Branch branch = Branch.builder()
                .id(UUID.randomUUID().toString())
                .code("CODE-TEST-"+ UUID.randomUUID())
                .bankEntityId("64bc71e6e9a5d42c0b3029d2")
                .locationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Branch Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .state("ACT")
                .creationDate(LocalDate.now())
                .uniqueKey(UUID.randomUUID().toString())
                .build();
        given(bankEntityService.createBranch(any(BranchReq.class)))
                .willReturn(branch);
        // when
        ResultActions response = mockMvc.perform(post(URL+"/addBranch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branchReq)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(branch.getId()))
                .andExpect(jsonPath("$.code").value(branch.getCode()))
                .andExpect(jsonPath("$.bankEntityId").value(branch.getBankEntityId()))
                .andExpect(jsonPath("$.locationId").value(branch.getLocationId()))
                .andExpect(jsonPath("$.name").value(branch.getName()))
                .andExpect(jsonPath("$.emailAddress").value(branch.getEmailAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(branch.getPhoneNumber()))
                .andExpect(jsonPath("$.line1").value(branch.getLine1()))
                .andExpect(jsonPath("$.line2").value(branch.getLine2()))
                .andExpect(jsonPath("$.uniqueKey").value(branch.getUniqueKey()))
                .andExpect(jsonPath("$.state").value(branch.getState()))
                .andExpect(jsonPath("$.creationDate").value(branch.getCreationDate().toString()))
                .andExpect(jsonPath("$.latitude").value(branch.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(branch.getLongitude()));
    }
    @Test
    void updateBranchTest() throws Exception {
        //given
        String code = "CODE-TEST-"+ UUID.randomUUID();
        BranchReq branchReq = BranchReq.builder()
                .name("Branch Test Updated")
                .build();
        Branch branchUpdated = Branch.builder()
                .id(UUID.randomUUID().toString())
                .code(code)
                .bankEntityId("64bc71e6e9a5d42c0b3029d2")
                .locationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name(branchReq.getName())
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .state("ACT")
                .creationDate(LocalDate.now())
                .uniqueKey(UUID.randomUUID().toString())
                .build();
        given(bankEntityService.updateBranch(code, branchReq))
                .willReturn(branchUpdated);
        //when
        ResultActions response = mockMvc.perform(put(URL+"/updateBranch/{code}", code)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branchReq)));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(branchUpdated.getId()))
                .andExpect(jsonPath("$.code").value(branchUpdated.getCode()))
                .andExpect(jsonPath("$.name").value(branchUpdated.getName()))
                .andExpect(jsonPath("$.uniqueKey").value(branchUpdated.getUniqueKey()));
    }

    @Test
    void updateBranchTestThrowException() throws Exception {
        //given
        String code = "CODE-TEST-"+ UUID.randomUUID();
        BranchReq branchReq = BranchReq.builder()
                .name("Branch Test Updated")
                .build();
        given(bankEntityService.updateBranch(code, branchReq))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put(URL+"/updateBranch/{code}", code)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branchReq)));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBranchTest() throws Exception {
        //given
        String code = "CODE-TEST-"+ UUID.randomUUID();
        Branch branchDeleted = Branch.builder()
                .id(UUID.randomUUID().toString())
                .code(code)
                .bankEntityId("64bc71e6e9a5d42c0b3029d2")
                .locationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Branch Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .state("INA")
                .creationDate(LocalDate.now())
                .uniqueKey(UUID.randomUUID().toString())
                .build();
        given(bankEntityService.deleteBranch(code)).willReturn(branchDeleted);
        //when
        ResultActions response = mockMvc.perform(put(URL+"/deleteBranch/{code}", code));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(branchDeleted.getState()));
    }

    @Test
    void deleteBranchTestThrowException() throws Exception {
        //given
        String code = "CODE-TEST-"+ UUID.randomUUID();
        given(bankEntityService.deleteBranch(code)).willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put(URL+"/deleteBranch/{code}", code));
        //then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findBranchByCodeBranchTest() throws Exception {
        //given
        String code = "CODE-TEST-"+ UUID.randomUUID();
        BranchRes branch = generateBranchRes();
        branch.setCode(code);
        given(bankEntityService.findBranchByCode(branch.getCode()))
                .willReturn(branch);
        //when
        ResultActions response = mockMvc.perform(get(URL+"/findBranch/{code}", code));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(branch)));
    }

    @Test
    void findAllbyStateBranchTest() throws Exception {
        //given
        String state = "ACT";
        BranchRes branch1 = generateBranchRes();
        branch1.setCode("CODE-TEST-"+ UUID.randomUUID());
        BranchRes branch2 = generateBranchRes();
        branch1.setCode("CODE-TEST-"+ UUID.randomUUID());
        List<BranchRes> branches = List.of(branch1, branch2);
        given(bankEntityService.findAllBranchesByState(state))
                .willReturn(branches);
        //when
        ResultActions response = mockMvc.perform(get(URL+"/findAllBranches/{state}", state));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(branches)));
    }

    @Test
    void findAllbyLocationBranchTest() throws Exception {
        //given
        String locationId = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";
        BranchRes branch1 = generateBranchRes();
        branch1.setCode("CODE-TEST-"+ UUID.randomUUID());
        BranchRes branch2 = generateBranchRes();
        branch1.setCode("CODE-TEST-"+ UUID.randomUUID());
        List<BranchRes> branches = List.of(branch1, branch2);
        given(bankEntityService.findAllBranchesByLocationId(locationId))
                .willReturn(branches);
        //when
        ResultActions response = mockMvc.perform(get(URL+"/findBranches-location/{locationId}", locationId));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(branches)));
    }

    @Test
    void findAllbyLocationAndStateBranchTest() throws Exception {
        //given
        String locationId = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";
        String state = "ACT";
        BranchRes branch1 = generateBranchRes();
        branch1.setCode("CODE-TEST-"+ UUID.randomUUID());
        BranchRes branch2 = generateBranchRes();
        branch1.setCode("CODE-TEST-"+ UUID.randomUUID());
        List<BranchRes> branches = List.of(branch1, branch2);
        given(bankEntityService.findAllBranchesByLocationIdAndState(locationId, state))
                .willReturn(branches);
        //when
        ResultActions response = mockMvc.perform(get(URL+"/Branches-location-state/{locationId}/{state}", locationId, state));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(branches)));
    }
    private BranchRes generateBranchRes() {
        return BranchRes.builder()
                .locationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .locationName("Location Test")
                .name("Branch Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
    }
}