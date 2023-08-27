package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.HolidayReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.HolidayRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoCountryRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.HolidayRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.HolidayMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @InjectMocks
    private HolidayService underTest;
    @Mock
    private HolidayRepository holidayRepository;
    @Mock
    private GeoLocationRepository geoLocationRepository;
    @Mock
    private GeoCountryRepository geoCountryRepository;
    @Mock
    private HolidayMapper holidayMapper;

    private HolidayReq holidayReq;

    @BeforeEach
    void setUp() {
        holidayReq = HolidayReq.builder()
                .holidayDate(new Date())
                .countryCode("EC")
                .geoLocationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Holiday")
                .type("Nacional")
                .build();

    }

    @Test
    void createHolidayTest() {
        //given
        given(holidayMapper.toHoliday(holidayReq)).willReturn(Holiday.builder()
                .holidayDate(holidayReq.getHolidayDate())
                .countryCode(holidayReq.getCountryCode())
                .geoLocationId(holidayReq.getGeoLocationId())
                .name(holidayReq.getName())
                .type(holidayReq.getType())
                .build());
        //when
        underTest.create(holidayReq);
        //then
        ArgumentCaptor<Holiday> holidayArgumentCaptor = ArgumentCaptor.forClass(Holiday.class);
        verify(holidayRepository).save(holidayArgumentCaptor.capture());
        Holiday holidayCaptured = holidayArgumentCaptor.getValue();
        assertThat(holidayCaptured.getName()).isEqualTo(holidayReq.getName());
    }

    @Test
    void updateHolidayTest() {
        //given
        String uuid = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";
        Holiday holiday = Holiday.builder()
                .holidayDate(new Date())
                .countryCode("EC")
                .geoLocationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Holiday")
                .type("Nacional")
                .build();
        given(holidayRepository.findByUuid(uuid)).willReturn(Optional.of(holiday));
        holiday.setName("Test Holiday Updated");
        //when
        underTest.update(uuid, holidayReq);
        //then
        ArgumentCaptor<Holiday> holidayArgumentCaptor = ArgumentCaptor.forClass(Holiday.class);
        verify(holidayRepository).save(holidayArgumentCaptor.capture());
        Holiday holidayCaptured = holidayArgumentCaptor.getValue();
        assertThat(holidayCaptured.getName()).isEqualTo(holiday.getName());
    }

    @Test
    void updateHolidayTestThrowExceptionWhenHolidayDoesNotExist() {
        //given
        String uuid = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";
        given(holidayRepository.findByUuid(uuid)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.update(uuid, holidayReq))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe el feriado");
        verify(holidayRepository, never()).save(any());

    }

    @Test
    void findbyCountryCodeHoliday() {
        //given
        String countryCode = "EC";
        Holiday holiday = Holiday.builder()
                .holidayDate(new Date())
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .state("ACT")
                .countryCode("EC")
                .geoLocationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Holiday")
                .type("Nacional")
                .build();
        given(holidayRepository.findByCountryCode(countryCode)).willReturn(List.of(holiday));
        HolidayRes holidayRes =  HolidayRes.builder()
                .holidayDate(holiday.getHolidayDate())
                .uuid(holiday.getUuid())
                .countryCode(holiday.getCountryCode())
                .geoLocationId(holiday.getGeoLocationId())
                .name(holiday.getName())
                .build();
        given(holidayMapper.toHolidayResList(List.of(holiday))).willReturn(List.of(holidayRes));
        GeoLocation geoLocation = GeoLocation.builder()
                .countryCode("EC")
                .levelCode("1")
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Location")
                .areaPhoneCode("02")
                .build();
        given(geoLocationRepository.findByUuid(holiday.getUuid())).willReturn(Optional.of(geoLocation));
        GeoCountry geoCountry = GeoCountry.builder()
                .countryCode("EC")
                .name("Ecuador")
                .phoneCode("593")
                .state("ACT")
                .build();
        given(geoCountryRepository.findByCountryCode(holiday.getCountryCode())).willReturn(Optional.of(geoCountry));
        //when
        List<HolidayRes> result = underTest.findbyCountryCode(countryCode);
        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findbyCountryCodeHolidayThrowExceptionWhenCountrCodeNoExist() {
        //given
        String countryCode = "TEST";
        given(holidayRepository.findByCountryCode(countryCode)).willReturn(List.of());
        //when
        //then
        assertThatThrownBy(() -> underTest.findbyCountryCode(countryCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe el código del pais");
    }

    @Test
    void findbyCountryCodeHolidayThrowExceptionWhenLocationNoExist() {
        //given
        String countryCode = "EC";
        Holiday holiday = Holiday.builder()
                .holidayDate(new Date())
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .state("ACT")
                .countryCode("EC")
                .geoLocationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Holiday")
                .type("Nacional")
                .build();
        given(holidayRepository.findByCountryCode(countryCode)).willReturn(List.of(holiday));
        HolidayRes holidayRes =  HolidayRes.builder()
                .holidayDate(holiday.getHolidayDate())
                .uuid(holiday.getUuid())
                .countryCode(holiday.getCountryCode())
                .geoLocationId(holiday.getGeoLocationId())
                .name(holiday.getName())
                .build();
        given(holidayMapper.toHolidayResList(List.of(holiday))).willReturn(List.of(holidayRes));
        given(geoLocationRepository.findByUuid(holiday.getUuid())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.findbyCountryCode(countryCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe la locación");
    }

    @Test
    void findbyCountryCodeHolidayThrowExceptionWhenCountryCodeNoExist() {
        //given
        String countryCode = "EC";
        Holiday holiday = Holiday.builder()
                .holidayDate(new Date())
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .state("ACT")
                .countryCode("EC")
                .geoLocationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Holiday")
                .type("Nacional")
                .build();
        given(holidayRepository.findByCountryCode(countryCode)).willReturn(List.of(holiday));
        HolidayRes holidayRes =  HolidayRes.builder()
                .holidayDate(holiday.getHolidayDate())
                .uuid(holiday.getUuid())
                .countryCode(holiday.getCountryCode())
                .geoLocationId(holiday.getGeoLocationId())
                .name(holiday.getName())
                .build();
        given(holidayMapper.toHolidayResList(List.of(holiday))).willReturn(List.of(holidayRes));
        GeoLocation geoLocation = GeoLocation.builder()
                .countryCode("EC")
                .levelCode("1")
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Location")
                .areaPhoneCode("02")
                .build();
        given(geoLocationRepository.findByUuid(holiday.getUuid())).willReturn(Optional.of(geoLocation));
        given(geoCountryRepository.findByCountryCode(holiday.getCountryCode())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.findbyCountryCode(countryCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe el codigo del pais");
    }
    @Test
    void findByLocationIdHolidayTest() {
        //given
        String geoLocationId = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";
        Holiday holiday = Holiday.builder()
                .holidayDate(new Date())
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .state("ACT")
                .countryCode("EC")
                .geoLocationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Holiday")
                .type("Nacional")
                .build();
        given(holidayRepository.findByGeoLocationId(geoLocationId)).willReturn(List.of(holiday));
        HolidayRes holidayRes =  HolidayRes.builder()
                .holidayDate(holiday.getHolidayDate())
                .uuid(holiday.getUuid())
                .countryCode(holiday.getCountryCode())
                .geoLocationId(holiday.getGeoLocationId())
                .name(holiday.getName())
                .build();
        given(holidayMapper.toHolidayResList(List.of(holiday))).willReturn(List.of(holidayRes));
        GeoCountry geoCountry = GeoCountry.builder()
                .countryCode("EC")
                .name("Ecuador")
                .phoneCode("593")
                .state("ACT")
                .build();
        given(geoCountryRepository.findByCountryCode(holiday.getCountryCode())).willReturn(Optional.of(geoCountry));
        GeoLocation geoLocation = GeoLocation.builder()
                .countryCode("EC")
                .levelCode("1")
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .name("Test Location")
                .areaPhoneCode("02")
                .build();
        given(geoLocationRepository.findByUuid(holiday.getUuid())).willReturn(Optional.of(geoLocation));
        //when
        List<HolidayRes> result = underTest.findByLocationId(geoLocationId);
        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findByLocationIdHolidayTestThrowExceptionWhenGeoLocationIdNoExist() {
        //given
        String geoLocationId = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";

        given(holidayRepository.findByGeoLocationId(geoLocationId)).willReturn(List.of());
        //when
        //then
        assertThatThrownBy(() -> underTest.findByLocationId(geoLocationId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe la locación");
    }

    @Test
    @SuppressWarnings("unchecked")
    void generateHolidaysWeekendsCountriesTest() {
        //given
        int year = 2021;
        int month = 1;
        boolean saturday = true;
        boolean sunday = true;
        String countryCode = "EC";
        GeoCountry geoCountry = GeoCountry.builder()
                .id("idTest")
                .countryCode("EC")
                .name("Ecuador")
                .phoneCode("593")
                .state("ACT")
                .build();
        given(geoCountryRepository.findByCountryCode(countryCode)).willReturn(Optional.of(geoCountry));
        //when
        underTest.generateHolidaysWeekendsCountries(year, month, saturday, sunday, countryCode);
        //then
        ArgumentCaptor<List<Holiday>> holidayArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(holidayRepository).saveAll(holidayArgumentCaptor.capture());
        List<Holiday> holidayCaptured = holidayArgumentCaptor.getValue();
        assertThat(holidayCaptured.size()).isGreaterThan(4);
    }
    @Test
    void generateHolidaysWeekendsCountriesTestThrowExceptionWhenCountryCodeNoExist() {
        //given
        int year = 2021;
        int month = 1;
        boolean saturday = true;
        boolean sunday = true;
        String countryCode = "EC";
        given(geoCountryRepository.findByCountryCode(countryCode)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.generateHolidaysWeekendsCountries(year, month, saturday, sunday, countryCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El pais con código: "+countryCode+" no existe");
    }

    @Test
    void deleteLogicHolidayTest() {
        //given
        Holiday holiday = Holiday.builder()
                .id("idTest")
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .holidayDate(holidayReq.getHolidayDate())
                .countryCode(holidayReq.getCountryCode())
                .geoLocationId(holidayReq.getGeoLocationId())
                .name(holidayReq.getName())
                .type(holidayReq.getType())
                .state("ACT")
                .build();
        given(holidayRepository.findByUuid(holiday.getUuid())).willReturn(Optional.of(holiday));
        //when
        underTest.deleteLogic(holiday.getUuid());
        //then
        ArgumentCaptor<Holiday> holidayArgumentCaptor = ArgumentCaptor.forClass(Holiday.class);
        verify(holidayRepository).save(holidayArgumentCaptor.capture());
        Holiday holidayCaptured = holidayArgumentCaptor.getValue();
        assertThat(holidayCaptured.getState()).isEqualTo("INA");
    }

    @Test
    void deleteLogicHolidayThrowExceptionWhenHolidayNoExist() {
        //given
        String uuid = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";
        given(holidayRepository.findByUuid(uuid)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteLogic(uuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe el feriado");
    }


    @Test
    void findByUuidHolidayTest() {
        //given
        String uuid = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";
        Holiday holiday = Holiday.builder()
                .id("idTest")
                .uuid("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .holidayDate(holidayReq.getHolidayDate())
                .countryCode(holidayReq.getCountryCode())
                .geoLocationId(holidayReq.getGeoLocationId())
                .name(holidayReq.getName())
                .type(holidayReq.getType())
                .state("ACT")
                .build();
        given(holidayRepository.findByUuid(uuid)).willReturn(Optional.of(holiday));
        //when
        Holiday result =  underTest.findByUuid(uuid);
        //then
        ArgumentCaptor<String> uuidArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(holidayRepository).findByUuid(uuidArgumentCaptor.capture());
        String uuidCaptured = uuidArgumentCaptor.getValue();
        assertThat(uuidCaptured).isEqualTo(uuid);
        assertThat(result).isEqualTo(holiday);
    }
    @Test
    void findByUuidHolidayThrowExceptionWhenHolidaydNoExist() {
        //given
        String uuid = "f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44";
        given(holidayRepository.findByUuid(uuid)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.findByUuid(uuid))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe el feriado");
    }
}