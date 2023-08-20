package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import ec.edu.espe.arquitectura.banquito.administration.repository.BankEntityRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.BranchRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.BankEntityMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
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
    @Disabled
    void createBranch() {
    }

    @Test
    @Disabled
    void updateBranch() {
    }

    @Test
    @Disabled
    void deleteBranch() {
    }

    @Test
    @Disabled
    void findBranchByCode() {
    }

    @Test
    @Disabled
    void findAllBranchesByState() {
    }

    @Test
    @Disabled
    void findAllBranchesByLocationId() {
    }

    @Test
    @Disabled
    void findAllBranchesByLocationIdAndState() {
    }
}