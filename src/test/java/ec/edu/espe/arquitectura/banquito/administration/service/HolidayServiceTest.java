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
    void findByLocationId() {
    }

    @Test
    void generateHolidaysWeekendsCountries() {
    }

    @Test
    void findHolidayByHolidayUnique() {
    }

    @Test
    void deleteLogic() {
    }

    @Test
    void findByUuid() {
    }
}