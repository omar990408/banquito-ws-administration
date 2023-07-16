package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GeoLocationRepository extends MongoRepository<GeoLocation,String> {
    @Query(value = "{'geoStructures.name': ?0,'countryCode': ?1}")
    List<GeoLocation> findByGeoStructureCountryCode(String name, String countryCode);
}
