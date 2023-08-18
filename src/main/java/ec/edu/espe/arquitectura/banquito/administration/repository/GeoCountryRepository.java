package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GeoCountryRepository extends MongoRepository<GeoCountry,String> {

    Optional<GeoCountry> findByCountryCode(String countryCode);
    List<GeoCountry> findAllByStateContaining(String state);

}
