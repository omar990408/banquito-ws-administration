package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
class BankEntityRepositoryTest {

    @Autowired
    private BankEntityRepository bankEntityRepositoryUnderTest;
    private BankEntity bankEntity;


    @Test
    @Disabled
    void findByInternationalCode() {
        //given
        bankEntity = BankEntity.builder()
                .name("Banco Test")
                .internationalCode("PCHQ")
                .build();
        bankEntityRepositoryUnderTest.save(bankEntity);
        //when
        Optional<BankEntity> result = bankEntityRepositoryUnderTest.findByInternationalCode("PCHQ");
        //then
        assertThat(result)
                .isPresent().
                hasValueSatisfying(bE -> {
                    assertThat(bE).usingRecursiveComparison().isEqualTo(bankEntity);
                });
    }
        @AfterEach
        void deleteSetUp() {
          bankEntityRepositoryUnderTest.delete(bankEntity);
        }
}