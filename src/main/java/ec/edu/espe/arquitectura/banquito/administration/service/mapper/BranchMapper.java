package ec.edu.espe.arquitectura.banquito.administration.service.mapper;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BranchRes;
import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchMapper {

    Branch toBranch(BranchReq branchReq);
    BranchRes toBranchRes(Branch branch);

    List<BranchRes> toBranchRes(List<Branch> branches);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBranch(BranchReq branchReq, @MappingTarget Branch branch);

}
