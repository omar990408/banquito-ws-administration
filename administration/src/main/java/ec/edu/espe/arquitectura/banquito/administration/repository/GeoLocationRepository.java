package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GeoLocationRepository extends MongoRepository<GeoLocation,String> {

    Optional <GeoLocation> findByCountryCodeAndName(String countryCode, String name);
    List<GeoLocation> findByCountryCodeAndLevelParentName(String countryCode, String levelParentName);
    Optional<GeoLocation> findByUuid(String uuid);
    List<GeoLocation> findByCountryCodeAndLevelCode(String countryCode, String levelCode);

}
