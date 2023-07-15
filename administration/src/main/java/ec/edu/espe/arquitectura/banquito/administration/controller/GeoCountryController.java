package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoCountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}