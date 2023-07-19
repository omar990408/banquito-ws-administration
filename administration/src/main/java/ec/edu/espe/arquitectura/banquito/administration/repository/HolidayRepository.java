package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.dto.res.HolidayRes;
import ec.edu.espe.arquitectura.banquito.administration.model.Holiday;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends MongoRepository<Holiday,String> {
    List<Holiday> findByCountryCode(String countryCode);
    List<Holiday> findByGeoLocationId(String geoLocationId);
    Optional<Holiday> findByUuid(String uuid);

}
