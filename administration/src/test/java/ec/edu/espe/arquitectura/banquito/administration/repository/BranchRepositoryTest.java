package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BranchRepositoryTest {

    @Autowired
    private BranchRepository branchRepository;
    private Branch branchFirst;

    @BeforeEach
    void setUp() {
        branchFirst = Branch.builder()
                .id(UUID.randomUUID().toString())
                .code("PICHIX0001")
                .bankEntityId("64b09cc56666b939a406a823")
                .locationId("11aff4b9-9df4-4f2c-8c11-df8ceaecc2a0")
                .name("Sucursal 0")
                .state("ACT")
                .emailAddress("test@mail.com")
                .phoneNumber("0987654321")
                .line1("Av. 6 de Diciembre")
                .line2("N34-56 y Av. 6 de Diciembre")
                .latitude(BigDecimal.valueOf(0.0))
                .longitude(BigDecimal.valueOf(0.0))
                .build();
    }

    //Probar guardar branch
    @DisplayName("Save Branch")
    @Test
    public void saveBranch() {
        //given - dado o condición previa o configuración
        Branch branch = Branch.builder()
                .id(UUID.randomUUID().toString())
                .code("PICHIX000121")
                .bankEntityId("64b09cc56666b939a406a823")
                .locationId("11aff4b9-9df4-4f2c-8c11-df8ceaecc2a0")
                .name("Sucursal 1")
                .state("ACT")
                .emailAddress("test@mail.com")
                .phoneNumber("0987654321")
                .line1("Av. 6 de Diciembre")
                .line2("N34-56 y Av. 6 de Diciembre")
                .latitude(BigDecimal.valueOf(0.0))
                .longitude(BigDecimal.valueOf(0.0))
                .build();
        //when - acción o el comportamiento que vamos a probar
        Branch branchTmp = branchRepository.save(branch);
        //then - verificar la salida
        assertThat(branchTmp).isNotNull();
    }

    @DisplayName("Test para obtener un empleado por ID")
    @Test
    public void findBranchByCode() {
        //given - dado o condición previa o configuración
        Branch branch = Branch.builder()
                .id(UUID.randomUUID().toString())
                .code("PICHIX0012")
                .bankEntityId("64b09cc56666b939a406a823")
                .locationId("11aff4b9-9df4-4f2c-8c11-df8ceaecc2a0")
                .name("Sucursal 1")
                .state("ACT")
                .emailAddress("test@mail.com")
                .phoneNumber("0987654321")
                .line1("Av. 6 de Diciembre")
                .line2("N34-56 y Av. 6 de Diciembre")
                .latitude(BigDecimal.valueOf(0.0))
                .longitude(BigDecimal.valueOf(0.0))
                .build();
        //when - acción o el comportamiento que vamos a probar
        Branch branchTmp = branchRepository.findByCode(branch.getCode()).get();
        //then - verificar la salida
        assertThat(branchTmp).isNotNull();
    }
}
