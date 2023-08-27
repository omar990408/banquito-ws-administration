package ec.edu.espe.arquitectura.banquito.administration.service.mapper;

import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankEntityMapper {
    BankEntityRes toBankEntityRes(BankEntity bankEntity);
}
