package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.GeoCountryReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BaseRes;
import ec.edu.espe.arquitectura.banquito.administration.exception.InsertException;
import ec.edu.espe.arquitectura.banquito.administration.service.GeoCountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/geoCountry")
public class GeoCountryController {
    private final GeoCountryService geoCountryService;

    @PostMapping("/create")
    public ResponseEntity<BaseRes> create(@RequestBody GeoCountryReq geoCountryReq) throws InsertException {
        return ResponseEntity.ok(geoCountryService.createCountry(geoCountryReq));
    }
}