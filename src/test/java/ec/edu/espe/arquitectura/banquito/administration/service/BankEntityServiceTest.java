package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BranchRes;
import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.repository.BankEntityRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.BranchRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.BankEntityMapper;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.BranchMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BankEntityServiceTest {

    @InjectMocks
    private BankEntityService underTest;
    @Mock
    private BankEntityRepository bankEntityRepository;

    @Mock
    private BankEntityMapper bankEntityMapper;
    @Mock
    private BranchMapper branchMapper;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private GeoLocationRepository geoLocationRepository;

    @Test
    void findBankEntityByInternationalCode() {
        //given
        String internationalCode = "BANKECQX";

        BankEntity bankEntity = BankEntity.builder().build();

        given(bankEntityRepository.findByInternationalCode(internationalCode)).willReturn(Optional.of(bankEntity));
        //when
        underTest.findBankEntityByInternationalCode(internationalCode);
        //then
        ArgumentCaptor<String> internationalCodeCaptor = ArgumentCaptor.forClass(String.class);
        verify(bankEntityRepository).findByInternationalCode(internationalCodeCaptor.capture());
        String internationalCodeArg = internationalCodeCaptor.getValue();
        assertThat(internationalCodeArg).isEqualTo(internationalCode);
    }
    @Test
    void findBankEntityByInternationalCodeThrowException() {
        //given
        String internationalCode = "BANKECQX";
        BankEntity bankEntity = BankEntity.builder().build();

        given(bankEntityRepository.findByInternationalCode(internationalCode)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(()->underTest.findBankEntityByInternationalCode(internationalCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("La entidad Bancaria con código: "+internationalCode+" no existe");
    }

    @Test
    void createBranch() {
        //given
        BranchReq branch = BranchReq.builder()
                .code("CODE-TEST-"+ UUID.randomUUID().toString())
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
        given(branchMapper.toBranch(branch)).willReturn(
                Branch.builder()
                        .code(branch.getCode())
                        .bankEntityId(branch.getBankEntityId())
                        .locationId(branch.getLocationId())
                        .name(branch.getName())
                        .emailAddress(branch.getEmailAddress())
                        .phoneNumber(branch.getPhoneNumber())
                        .line1(branch.getLine1())
                        .line2(branch.getLine2())
                        .latitude(branch.getLatitude())
                        .longitude(branch.getLongitude())
                        .build()
        );
        //when
        underTest.createBranch(branch);
        //then
        ArgumentCaptor<Branch> branchArgumentCaptor = ArgumentCaptor.forClass(Branch.class);
        verify(branchRepository).save(branchArgumentCaptor.capture());
        Branch capturedBranch =  branchArgumentCaptor.getValue();
        assertThat(capturedBranch.getCode()).isEqualTo(branch.getCode());
    }

    @Test
    void createBranchThrowException() {
        //given
        BranchReq branch = BranchReq.builder()
                .code("CODE-TEST-"+ UUID.randomUUID().toString())
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
        given(branchRepository.findByCode(branch.getCode())).willReturn(Optional.of(mock(Branch.class)));
        //when
        //then
        assertThatThrownBy(() -> underTest.createBranch(branch))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("La sucursal con código: "+branch.getCode()+" ya existe");
    }

    @Test
    void updateBranch() {
        //given
        String code = "PICHIXXBQ1000";
        BranchReq branchReq = BranchReq.builder()
                .name("Branch Update Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
        Branch branch = Branch.builder()
                .name("Branch Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
        branch.setName(branchReq.getName());
        given(branchRepository.findByCode(code)).willReturn(Optional.of(branch));
        //when
        underTest.updateBranch(code, branchReq);
        //then
        ArgumentCaptor<Branch> branchArgumentCaptor = ArgumentCaptor.forClass(Branch.class);
        verify(branchRepository).save(branchArgumentCaptor.capture());
        Branch capturedBranch =  branchArgumentCaptor.getValue();
        assertThat(capturedBranch.getName()).isEqualTo(branch.getName());
    }

    @Test
    void updateBranchThrowException() {
        //given
        String code = "codeNoExist";
        BranchReq branchReq = BranchReq.builder()
                .name("Branch Update Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
        Branch branch = Branch.builder()
                .name("Branch Test")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
        branch.setName(branchReq.getName());
        given(branchRepository.findByCode(code)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.updateBranch(code, branchReq))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe la sucursal con código: "+code);
    }

    @Test
    void deleteBranch() {
//given
        String code = "CODE-TEST-"+ UUID.randomUUID().toString();
        Branch branch = Branch.builder()
                .code(code)
                .bankEntityId("64bc71e6e9a5d42c0b3029d2")
                .locationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Branch Test")
                .state("ACT")
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
        given(branchRepository.findByCode(code)).willReturn(Optional.of(branch));
        //when
        underTest.deleteBranch(code);
        //then
        ArgumentCaptor<Branch> branchArgumentCaptor = ArgumentCaptor.forClass(Branch.class);
        verify(branchRepository).save(branchArgumentCaptor.capture());
        Branch capturedBranch =  branchArgumentCaptor.getValue();
        assertThat(capturedBranch.getState()).isEqualTo("INA");
    }

    @Test
    void findBranchByCode() {
        //given
        String code = "PICHIXXBQ1001";
        Branch branch = Branch.builder()
                .code(code)
                .bankEntityId("64b09cc56666b939a406a823")
                .locationId("11aff4b9-9df4-4f2c-8c11-df8ceaecc2a0")
                .name("Branch Test")
                .state("ACT")
                .build();
        given(branchRepository.findByCode(code)).willReturn(Optional.of(branch));
        BranchRes branchRes = BranchRes.builder()
                .code(code)
                .locationId(branch.getLocationId())
                .name(branch.getName())
                .locationName("")
                .build();
        given(branchMapper.toBranchRes(branch)).willReturn(branchRes);
        given(geoLocationRepository.findByUuid(branch.getLocationId())).willReturn(Optional.of(GeoLocation.builder()
                        .name("Location Test")
                .build()));
        //when
        BranchRes response =  underTest.findBranchByCode(code);
        //then
        assertThat(response).isNotNull();
    }

    @Test
    void findBranchByCodeThrowException() {
        //given
        String code = "PICHIXXBQ1001";
        Branch branch = Branch.builder()
                .code(code)
                .bankEntityId("64b09cc56666b939a406a823")
                .locationId("11aff4b9-9df4-4f2c-8c11-df8ceaecc2a0")
                .name("Branch Test")
                .state("ACT")
                .build();
        given(branchRepository.findByCode(code)).willReturn(Optional.of(branch));
        BranchRes branchRes = BranchRes.builder()
                .code(code)
                .locationId(branch.getLocationId())
                .name(branch.getName())
                .locationName("")
                .build();
        given(branchMapper.toBranchRes(branch)).willReturn(branchRes);
        given(geoLocationRepository.findByUuid(branch.getLocationId())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.findBranchByCode(code))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe la locación con código: "+branch.getLocationId());
    }

    @Test
    void findAllBranchesByState() {
        //when
        underTest.findAllBranchesByState("ACT");
        //then
        verify(branchRepository).findAllByStateContaining("ACT");
    }

    @Test
    void findAllBranchesByLocationId() {
        //given
        String locationId = "11aff4b9-9df4-4f2c-8c11-df8ceaecc2a0";
        //when
        underTest.findAllBranchesByLocationId(locationId);
        //then
        verify(branchRepository).findByLocationId(locationId);
    }

    @Test
    void findAllBranchesByLocationIdAndState() {
        //given
        String locationId = "11aff4b9-9df4-4f2c-8c11-df8ceaecc2a0";
        String state = "ACT";
        //when
        underTest.findAllBranchesByLocationIdAndState(locationId, state);
        //then
        verify(branchRepository).findByLocationIdAndState(locationId, state);
    }
}