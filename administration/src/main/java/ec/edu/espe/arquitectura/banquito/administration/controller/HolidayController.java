package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.HolidayReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.HolidayRes;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import ec.edu.espe.arquitectura.banquito.administration.service.HolidayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/holiday")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping("/create")
    public ResponseEntity<Holiday> create(@RequestBody HolidayReq holidayReq){
        try{
            Holiday holiday = this.holidayService.create(holidayReq);
            return ResponseEntity.ok().body(holiday);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<Holiday> update (@PathVariable String uuid, @RequestBody HolidayReq holidayReq){
        try{
            Holiday holiday = this.holidayService.update(uuid, holidayReq);
            return ResponseEntity.ok().body(holiday);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findByCountryCode/{countryCode}")
    public  ResponseEntity<List<HolidayRes>> findByCountryCode(@PathVariable String countryCode){
        List<HolidayRes> holidayRes = this.holidayService.findbyCountryCode(countryCode);
        return ResponseEntity.ok().body(holidayRes);
    }

    @GetMapping("/findByGeoLocationId/{geoLocationId}")
    public  ResponseEntity<List<HolidayRes>> findByGeoLocationId(@PathVariable String geoLocationId){
        List<HolidayRes> holidayRes = this.holidayService.findByLocationId(geoLocationId);
        return ResponseEntity.ok().body(holidayRes);
    }

    @PostMapping("/holidayCountry/generate-weekends/{id}")
    public ResponseEntity<List<Holiday>> generateHolidays(
            @PathVariable String id,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(defaultValue = "false") Boolean saturday,
            @RequestParam(defaultValue = "false") Boolean sunday){
        return ResponseEntity.ok().body(this.holidayService.generateHolidaysWeekendsCountries(year, month, saturday, sunday,id));
    }

    @PutMapping("/delete/{uuid}")
    public ResponseEntity<Holiday> delete(@PathVariable String uuid){
        return ResponseEntity.ok().body(this.holidayService.deleteLogic(uuid));
    }
}

