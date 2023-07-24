package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoCountryRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoCountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/findByCountryCode/{code}")
    public ResponseEntity<GeoCountryRes> findByCountryCode(@PathVariable String code){
        try{
            GeoCountryRes geoCountryRes = this.geoCountryService.findByCountryCode(code);
            return ResponseEntity.ok().body(geoCountryRes);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/findCountriesList")
    public ResponseEntity<List<GeoCountryRes>> findCountries(){
        try{
            List<GeoCountryRes>geoCountryRes=this.geoCountryService.getCountries();
            return ResponseEntity.ok().body(geoCountryRes);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }

    }

}