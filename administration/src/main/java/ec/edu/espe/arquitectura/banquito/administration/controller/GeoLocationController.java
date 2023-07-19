package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.HolidayReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoLocationReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoLocationRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoLocationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/geoLocation")
public class GeoLocationController {
    private final GeoLocationService geoLocationService;

    public GeoLocationController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeoLocation> create(@RequestBody GeoLocationReq geoLocationReq){
        try{
            GeoLocation geoLocation = this.geoLocationService.create(geoLocationReq);
            return ResponseEntity.ok().body(geoLocation);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeoLocation> update(@PathVariable String id, @RequestBody GeoLocationReq geoLocationReq){
        try{
            GeoLocation geoLocation = this.geoLocationService.update(id, geoLocationReq);
            return ResponseEntity.ok().body(geoLocation);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/findByCountryCodeAndName")
    public ResponseEntity<GeoLocationRes> findByCountryCodeAndName(@RequestParam String countryCode, @RequestParam String name){
        GeoLocationRes geoLocationRes = this.geoLocationService.findByCountryCodeAndName(countryCode, name);
        return ResponseEntity.ok().body(geoLocationRes);
    }

    @GetMapping("/CountryCodeAndParentName")
    public ResponseEntity<List<GeoLocationRes>> findByCountryCodeAndParentName(@RequestParam String countryCode, @RequestParam String parentName){
        List<GeoLocationRes> geoLocationRes = this.geoLocationService.findByCountryCodeAndLevelParentName(countryCode, parentName);
        return ResponseEntity.ok().body(geoLocationRes);
    }

    @GetMapping("/findByUuid/{uuid}")
    public ResponseEntity<GeoLocationRes> findByUuid(@PathVariable String uuid){
        GeoLocationRes geoLocationRes = this.geoLocationService.findByUuid(uuid);
        return ResponseEntity.ok().body(geoLocationRes);
    }

    @PutMapping("/addHoliday/{id}")
    public ResponseEntity<Holiday> addHoliday(@PathVariable String id, @RequestBody HolidayReq holidayReq){
        try{
            GeoLocation geoLocation = this.geoLocationService.addHoliday(id, holidayReq);
            return ResponseEntity.ok().body(geoLocation.getHolidays().get(geoLocation.getHolidays().size()-1));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateHoliday/{id}")
    public ResponseEntity<GeoLocation> updateHoliday(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestBody HolidayReq holidayReq){
        try{
            GeoLocation geoLocation = this.geoLocationService.updateHoliday(id,date, holidayReq);
            return ResponseEntity.ok().body(geoLocation);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/holiday/generate-weekends/{id}")
    public ResponseEntity<GeoLocation> generateHolidays(
            @PathVariable String id,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(defaultValue = "false") Boolean saturday,
            @RequestParam(defaultValue = "false") Boolean sunday){
        return ResponseEntity.ok().body(this.geoLocationService.generateHolidaysWeekends(id, year, month, saturday, sunday));
    }
}
