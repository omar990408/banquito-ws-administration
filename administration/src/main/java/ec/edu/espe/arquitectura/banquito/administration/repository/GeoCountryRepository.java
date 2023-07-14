package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.GeoCountry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeoCountryRepository extends MongoRepository<GeoCountry,String> {

}
