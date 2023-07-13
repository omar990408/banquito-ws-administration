package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankEntityRepository extends MongoRepository<BankEntity,String> {
}
