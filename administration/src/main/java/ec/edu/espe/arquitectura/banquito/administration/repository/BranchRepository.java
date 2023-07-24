package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends MongoRepository<Branch,String> {
    Optional<Branch> findByCode(String code);
    List<Branch> findAllByStateContaining(String state);
    List<Branch> findByLocationId(String locationId);
    List<Branch> findByLocationIdAndState(String locationId, String state);

}
