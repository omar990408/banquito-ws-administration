package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.HolidayReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoCountryRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoCountryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("api/v1/geoCountry")
public class GeoCountryController {
    private final GeoCountryService geoCountryService;

    public GeoCountryController(GeoCountryService geoCountryService) {
        this.geoCountryService = geoCountryService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeoCountry> create(@RequestBody GeoCountryReq geoCountryReq){
        try{
            GeoCountry geoCountry = this.geoCountryService.create(geoCountryReq);
            return ResponseEntity.ok().body(geoCountry);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<GeoCountry> update(@PathVariable String code, @RequestBody GeoCountryReq geoCountryReq){
        try{
            GeoCountry geoCountry = this.geoCountryService.update(code, geoCountryReq);
            return ResponseEntity.ok().body(geoCountry);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{code}")
    public void delete(@PathVariable String code){
        this.geoCountryService.delete(code);
    }

    @PutMapping("/addHoliday/{code}")
    public ResponseEntity<Holiday> addHoliday(@PathVariable String code, @RequestBody HolidayReq holidayReq){
        try{
            GeoCountry geoCountry = this.geoCountryService.addHoliday(code, holidayReq);
            return ResponseEntity.ok().body(geoCountry.getHolidays().get(geoCountry.getHolidays().size()-1));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateHoliday/{code}")
    public ResponseEntity<GeoCountry> updateHoliday(
            @PathVariable String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestBody HolidayReq holidayReq){
        try{
            GeoCountry geoCountry = this.geoCountryService.updateHoliday(code,date, holidayReq);
            return ResponseEntity.ok().body(geoCountry);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/holiday/generate-weekends/{code}")
    public ResponseEntity<GeoCountry> generateHolidays(
            @PathVariable String code,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(defaultValue = "false") Boolean saturday,
            @RequestParam(defaultValue = "false") Boolean sunday){
        return ResponseEntity.ok().body(this.geoCountryService.generateHolidaysWeekends(code, year, month, saturday, sunday));
    }


    @GetMapping("/findByCountryCode/{code}")
    public ResponseEntity<GeoCountryRes> findByCountryCode(@PathVariable String code){
        try{
            GeoCountryRes geoCountryRes = this.geoCountryService.findByCountryCode(code);
            return ResponseEntity.ok().body(geoCountryRes);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

}