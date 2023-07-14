package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Optional;


public interface BankEntityRepository extends MongoRepository<BankEntity,String> {
    Optional<BankEntity> findByInternationalCode(String internationalCode);
}
