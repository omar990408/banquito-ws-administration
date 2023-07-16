package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoLocationReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.GeoLocationRes;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoLocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/findByCountryCodeAndName")
    public ResponseEntity<List<GeoLocationRes>> findByCountryCodeAndName(@RequestParam String name, @RequestParam String countryCode){
        try{
            List<GeoLocationRes> geoLocationRes = this.geoLocationService.findByCountryCodeAndName(name, countryCode);
            return ResponseEntity.ok().body(geoLocationRes);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }
}
