package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoStructureReq;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoStructure;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoCountryRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoCountryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GeoCountryServiceTest {

    @InjectMocks
    private GeoCountryService underTest;
    @Mock
    private GeoCountryRepository geoCountryRepository;
    @Mock
    private GeoCountryMapper geoCountryMapper;
    private GeoCountryReq geoCountryReq;

    @BeforeEach
    void setUp() {
        geoCountryReq = GeoCountryReq.builder()
                .countryCode("EC")
                .name("Ecuador")
                .phoneCode("593")
                .geoStructures(new ArrayList<>())
                .build();
        GeoStructureReq provincia = GeoStructureReq.builder()
                .levelCode(1)
                .name("Provincia")
                .build();
        geoCountryReq.getGeoStructures().add(provincia);
        GeoStructureReq canton = GeoStructureReq.builder()
                .levelCode(2)
                .name("Canton")
                .build();
        geoCountryReq.getGeoStructures().add(canton);
        GeoStructureReq parroquia = GeoStructureReq.builder()
                .levelCode(3)
                .name("Parroquia")
                .build();
        geoCountryReq.getGeoStructures().add(parroquia);
    }

    @Test
    void createCountryTest() {
        // Given
        given(geoCountryRepository.findByCountryCode(geoCountryReq.getCountryCode()))
                .willReturn(Optional.empty());
        given(geoCountryMapper.toGeoCountry(geoCountryReq)).willReturn(GeoCountry.builder()
                .name(geoCountryReq.getName())
                .countryCode(geoCountryReq.getCountryCode())
                .phoneCode(geoCountryReq.getPhoneCode())
                .geoStructures(geoCountryReq.getGeoStructures().stream()
                        .map(geoStructureReq -> GeoStructure.builder()
                                        .levelCode(geoStructureReq.getLevelCode())
                                        .name(geoStructureReq.getName())
                                        .build())
                        .collect(Collectors.toList()))
                .build());
        // When
        underTest.create(geoCountryReq);
        // Then
        ArgumentCaptor<GeoCountry> geoCountryArgumentCaptor = ArgumentCaptor.forClass(GeoCountry.class);
        verify(geoCountryRepository).save(geoCountryArgumentCaptor.capture());
        GeoCountry geoCountry = geoCountryArgumentCaptor.getValue();
        assertEquals(geoCountryReq.getName(), geoCountry.getName());
    }

    @Test
    void createCountryTestThrowExceptionWhenCountryExist() {
        // Given
        given(geoCountryRepository.findByCountryCode(geoCountryReq.getCountryCode()))
                .willReturn(Optional.of(GeoCountry.builder().build()));
        // When
        // Then
        assertThatThrownBy(() -> underTest.create(geoCountryReq))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ya existe un pais con el codigo: "+ geoCountryReq.getCountryCode());
    }
    @Test
    void updateCountryTest() {
        //Given
        String code = "EC";
        GeoCountry geoCountry = GeoCountry.builder()
                .name("Ecuador")
                .state("ACT")
                .countryCode("EC")
                .phoneCode("5931")
                .geoStructures(geoCountryReq.getGeoStructures().stream()
                        .map(geoStructureReq -> GeoStructure.builder()
                                .levelCode(geoStructureReq.getLevelCode())
                                .name(geoStructureReq.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        given(geoCountryRepository.findByCountryCode(code))
                .willReturn(Optional.of(geoCountry));
        geoCountry.setPhoneCode(geoCountryReq.getPhoneCode());
        //When
        underTest.update(code, geoCountryReq);
        //Then
        ArgumentCaptor<GeoCountry> geoCountryArgumentCaptor = ArgumentCaptor.forClass(GeoCountry.class);
        verify(geoCountryRepository).save(geoCountryArgumentCaptor.capture());
        GeoCountry geoCountryCaptured = geoCountryArgumentCaptor.getValue();
        assertEquals(geoCountryReq.getPhoneCode(), geoCountryCaptured.getPhoneCode());
    }

    @Test
    void updateCountryTestThrowExceptionWhenCountryDoesNotExist() {
        //Given
        String code = "XA";
        given(geoCountryRepository.findByCountryCode(code))
                .willReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> underTest.update(code, geoCountryReq))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El pais con código: "+code+" no existe");
    }

    @Test
    void deleteLogicCountryTest() {
        //Given
        String code = "EC";
        GeoCountry geoCountry = GeoCountry.builder()
                .name("Ecuador")
                .state("ACT")
                .countryCode("EC")
                .phoneCode("5931")
                .geoStructures(geoCountryReq.getGeoStructures().stream()
                        .map(geoStructureReq -> GeoStructure.builder()
                                .levelCode(geoStructureReq.getLevelCode())
                                .name(geoStructureReq.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        given(geoCountryRepository.findByCountryCode(code))
                .willReturn(Optional.of(geoCountry));
        //When
        underTest.deleteLogic(code);
        //Then
        ArgumentCaptor<GeoCountry> geoCountryArgumentCaptor = ArgumentCaptor.forClass(GeoCountry.class);
        verify(geoCountryRepository).save(geoCountryArgumentCaptor.capture());
        GeoCountry geoCountryCaptured = geoCountryArgumentCaptor.getValue();
        assertEquals("INA", geoCountryCaptured.getState());
    }

    @Test
    void findByCountryCode() {
        //Given
        String code = "EC";
        GeoCountry geoCountry = GeoCountry.builder()
                .name("Ecuador")
                .state("ACT")
                .countryCode("EC")
                .phoneCode("593")
                .geoStructures(geoCountryReq.getGeoStructures().stream()
                        .map(geoStructureReq -> GeoStructure.builder()
                                .levelCode(geoStructureReq.getLevelCode())
                                .name(geoStructureReq.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        given(geoCountryRepository.findByCountryCode(code)).willReturn(Optional.of(geoCountry));
        //When
        underTest.findByCountryCode(code);
        //Then
        ArgumentCaptor<GeoCountry> geoCountryArgumentCaptor = ArgumentCaptor.forClass(GeoCountry.class);
        verify(geoCountryMapper).toGeoCountryRes(geoCountryArgumentCaptor.capture());
        GeoCountry geoCountryCaptured = geoCountryArgumentCaptor.getValue();
        assertEquals(geoCountry.getName(), geoCountryCaptured.getName());
    }

    @Test
    void getCountries() {
        //when
        underTest.getCountries();
        //then
        verify(geoCountryRepository).findAllByStateContaining("ACT");
    }
}