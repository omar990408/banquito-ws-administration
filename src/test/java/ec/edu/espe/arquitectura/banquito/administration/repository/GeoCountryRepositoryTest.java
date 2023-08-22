package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataMongoTest
class GeoCountryRepositoryTest {
    @Autowired
    private GeoCountryRepository geoCountryRepositoryUnderTest;
    private GeoCountry geoCountry;
    @BeforeEach
    void setUp() {
        geoCountry = GeoCountry.builder()
                .countryCode("TEST")
                .name("Country Test")
                .phoneCode("593")
                .state("ACT")
                .build();
    }

    @AfterEach
    void tearDown() {
        geoCountryRepositoryUnderTest.delete(geoCountry);
    }

    @Test
    @Disabled
    void findByCountryCode() {
        //given
        geoCountryRepositoryUnderTest.save(geoCountry);
        //when
        Optional<GeoCountry> result = geoCountryRepositoryUnderTest.findByCountryCode(geoCountry.getCountryCode());
        //then
        assertThat(result)
                .isPresent().
                hasValueSatisfying(gC -> {
                    assertThat(gC).usingRecursiveComparison().isEqualTo(geoCountry);
                });
    }

    @Test
    @Disabled
    void findByCountryCodeWhenCountryCodeDoesNotExist() {
        //given
        geoCountryRepositoryUnderTest.save(geoCountry);
        String countryCode = "TEST-CODE-NOT-EXIST";
        //when
        Optional<GeoCountry> result = geoCountryRepositoryUnderTest.findByCountryCode(countryCode);
        //then
        assertThat(result).isNotPresent();
    }

    @Test
    @Disabled
    void findAllByStateContaining() {
          //given
          geoCountryRepositoryUnderTest.save(geoCountry);
          //when
        List<GeoCountry> result = geoCountryRepositoryUnderTest.findAllByStateContaining(geoCountry.getState());
          //then
        assertThat(result)
                .isNotEmpty()
                .contains(geoCountry);
    }
}