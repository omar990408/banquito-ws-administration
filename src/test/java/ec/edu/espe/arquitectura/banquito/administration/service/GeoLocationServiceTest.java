package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoLocationReq;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.GeoLocationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GeoLocationServiceTest {

    @InjectMocks
    private GeoLocationService underTest;
    @Mock
    private GeoLocationRepository geoLocationRepository;
    @Mock
    private GeoLocationMapper geoLocationMapper;

    private GeoLocationReq geoLocationReq;

    @BeforeEach
    void setUp() {
        geoLocationReq = GeoLocationReq.builder()
                .countryCode("EC")
                .levelCode("1")
                .levelName("Provincia")
                .name("Pichincha")
                .areaPhoneCode("02")
                .build();
    }

    @Test
    void createGeoLocationTest() {
        //given
        given(geoLocationRepository.findByCountryCodeAndName(geoLocationReq.getCountryCode(), geoLocationReq.getName()))
                .willReturn(Optional.empty());
        given(geoLocationMapper.toGeoLocation(geoLocationReq)).willReturn(GeoLocation.builder()
                .countryCode(geoLocationReq.getCountryCode())
                .levelCode(geoLocationReq.getLevelCode())
                .levelName(geoLocationReq.getLevelName())
                .name(geoLocationReq.getName())
                .areaPhoneCode(geoLocationReq.getAreaPhoneCode())
                .build());
        //when
        underTest.create(geoLocationReq);
        //then
        ArgumentCaptor<GeoLocation> geoLocationArgumentCaptor = ArgumentCaptor.forClass(GeoLocation.class);
        verify(geoLocationRepository).save(geoLocationArgumentCaptor.capture());
        GeoLocation geoLocation = geoLocationArgumentCaptor.getValue();
        assertEquals(geoLocationReq.getCountryCode(), geoLocation.getCountryCode());
    }
    @Test
    void createGeoLocationTestThorowExceptionWhenGeoLocationExist() {
        //given
        given(geoLocationRepository.findByCountryCodeAndName(geoLocationReq.getCountryCode(), geoLocationReq.getName()))
                .willReturn(Optional.of(GeoLocation.builder().build()));
        //when
        //then
        assertThatThrownBy(() -> underTest.create(geoLocationReq))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ya existe un registro con el mismo nombre");
        verify(geoLocationRepository, never()).save(any());
    }

    @Test
    void updateGeoLocationTest() {
        //given
        String id = "TestId";
        GeoLocation geoLocation = GeoLocation.builder()
                .countryCode("EC")
                .levelCode("1")
                .levelName("Provincia")
                .name("Pichincha")
                .areaPhoneCode("03")
                .build();
        given(geoLocationRepository.findById(id)).willReturn(Optional.of(geoLocation));
        geoLocation.setAreaPhoneCode(geoLocationReq.getAreaPhoneCode());
        //when
        underTest.update(id, geoLocationReq);
        //then
        ArgumentCaptor<GeoLocation> geoLocationArgumentCaptor = ArgumentCaptor.forClass(GeoLocation.class);
        verify(geoLocationRepository).save(geoLocationArgumentCaptor.capture());
        GeoLocation geoLocationCaptured = geoLocationArgumentCaptor.getValue();
        assertEquals(geoLocationReq.getAreaPhoneCode(), geoLocationCaptured.getAreaPhoneCode());
    }

    @Test
    void updateGeoLocationTestThrowExceptionWhenGeoLocationDoesNotExist() {
        //given
        String id = "TestId";
        given(geoLocationRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.update(id, geoLocationReq))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No existe la locación");
        verify(geoLocationRepository, never()).save(any());
    }
    @Test
    void findByCountryCodeAndNameTest() {
        //given
        String countryCode = "EC";
        String name = "Pichincha";
        GeoLocation geoLocation = GeoLocation.builder()
                .countryCode("EC")
                .levelCode("1")
                .levelName("Provincia")
                .name("Pichincha")
                .areaPhoneCode("02")
                .build();
        given(geoLocationRepository.findByCountryCodeAndName(countryCode, name)).willReturn(Optional.of(geoLocation));
        //when
        underTest.findByCountryCodeAndName(countryCode, name);
        //then
        ArgumentCaptor<GeoLocation> geoLocationArgumentCaptor = ArgumentCaptor.forClass(GeoLocation.class);
        verify(geoLocationMapper).toGeoLocationRes(geoLocationArgumentCaptor.capture());
        GeoLocation geoLocationCaptured = geoLocationArgumentCaptor.getValue();
        assertEquals(geoLocation.getName(), geoLocationCaptured.getName());
    }
    @Test
    void findByCountryCodeAndNameTestThrowExceptionWhenGeoLocationNotFound() {
        //given
        String countryCode = "EC";
        String name = "Test";
        given(geoLocationRepository.findByCountryCodeAndName(countryCode, name)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.findByCountryCodeAndName(countryCode, name))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No se encontro resultados");
    }

    @Test
    @SuppressWarnings("unchecked")
    void findByCountryCodeAndLevelParentNameTest() {
        //given
        String countryCode = "EC";
        String levelParentName = "Provincia";
        GeoLocation geoLocation = GeoLocation.builder()
                .id("TestId")
                .countryCode("EC")
                .levelParentId("1")
                .levelParentName("Provincia")
                .levelCode("2")
                .levelName("Canton")
                .name("Quito")
                .areaPhoneCode("02")
                .build();
        GeoLocation geoLocation2 = GeoLocation.builder()
                .id("TestId2")
                .countryCode("EC")
                .levelParentId("1")
                .levelParentName("Provincia")
                .levelCode("2")
                .levelName("Canton")
                .name("Cayambe")
                .areaPhoneCode("02")
                .build();
        given(geoLocationRepository.findByCountryCodeAndLevelParentName(countryCode, levelParentName))
                .willReturn(List.of(geoLocation, geoLocation2));
        //when
        underTest.findByCountryCodeAndLevelParentName(countryCode, levelParentName);
        //then
        ArgumentCaptor<List<GeoLocation>> geoLocationArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(geoLocationMapper).toGeoLocationResList(geoLocationArgumentCaptor.capture());
        List<GeoLocation> geoLocationCaptured = geoLocationArgumentCaptor.getValue();
        assertEquals(2, geoLocationCaptured.size());
    }

    @Test
    void findByUuidGeoLocationTest() {
        //given
        String uuid = "aaaa-bbbb-cccc-dddd";
        GeoLocation geoLocation = GeoLocation.builder()
                .id("TestId")
                .uuid("aaaa-bbbb-cccc-dddd")
                .countryCode("EC")
                .levelParentId("1")
                .levelParentName("Provincia")
                .levelCode("2")
                .levelName("Canton")
                .name("Quito")
                .areaPhoneCode("02")
                .build();
        given(geoLocationRepository.findByUuid(uuid)).willReturn(Optional.of(geoLocation));
        //when
        underTest.findByUuid(uuid);
        //then
        ArgumentCaptor<GeoLocation> geoLocationArgumentCaptor = ArgumentCaptor.forClass(GeoLocation.class);
        verify(geoLocationMapper).toGeoLocationRes(geoLocationArgumentCaptor.capture());
        GeoLocation geoLocationCaptured = geoLocationArgumentCaptor.getValue();
        assertEquals(geoLocation.getName(), geoLocationCaptured.getName());
    }

    @Test
    void findByUuidGeoLocationTestThrowExceptionWhenNotFound() {
        //given
        String uuid = "aaaa-bbbb-cccc-dddd";
        given(geoLocationRepository.findByUuid(uuid)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.findByUuid(uuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No se encontro resultados");
    }

    @Test
    @SuppressWarnings("unchecked")
    void findByCountryCodeAndLevelCodeGeoLocationTest() {
        //given
        String countryCode = "EC";
        String levelCode = "1";
        GeoLocation geoLocation = GeoLocation.builder()
                .id("TestId")
                .countryCode("EC")
                .levelParentId("1")
                .levelParentName("Provincia")
                .levelCode("2")
                .levelName("Canton")
                .name("Quito")
                .areaPhoneCode("02")
                .build();
        given(geoLocationRepository.findByCountryCodeAndLevelCode(countryCode, levelCode))
                .willReturn(List.of(geoLocation));
        //when
        underTest.findByCountryCodeAndLevelCode(countryCode, levelCode);
        //then
        ArgumentCaptor<List<GeoLocation>> geoLocationArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(geoLocationMapper).toGeoLocationResList(geoLocationArgumentCaptor.capture());
        List<GeoLocation> geoLocationCaptured = geoLocationArgumentCaptor.getValue();
        assertEquals(1, geoLocationCaptured.size());
    }
    @Test
    void findByCountryCodeAndLevelCodeGeoLocationTestThrowExceptionWhenNotFound() {
        //given
        String countryCode = "EC";
        String levelCode = "1";
        given(geoLocationRepository.findByCountryCodeAndLevelCode(countryCode, levelCode))
                .willReturn(List.of());
        //when
        //then
        assertThatThrownBy(() -> underTest.findByCountryCodeAndLevelCode(countryCode, levelCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No se encontro resultados");
    }
}