package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.controller.dto.req.BankEntityReq;
import ec.edu.espe.arquitectura.banquito.administration.controller.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.controller.dto.res.BaseRes;
import ec.edu.espe.arquitectura.banquito.administration.exception.InsertException;
import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import ec.edu.espe.arquitectura.banquito.administration.repository.BankEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ec.edu.espe.arquitectura.banquito.administration.constant.MessageConstant.ERROR_REGISTRY;
import static ec.edu.espe.arquitectura.banquito.administration.constant.MessageConstant.SUCCESS_REGISTRY;

@Service
@RequiredArgsConstructor
public class BankEntityService {
    private final BankEntityRepository bankEntityRepository;

    public BaseRes createBankEntity(BankEntityReq bankEntityReq) throws InsertException {
        try{
            BankEntity bankEntity = toBankEntity(bankEntityReq);
            bankEntityRepository.save(bankEntity);
            return BaseRes.builder().message(SUCCESS_REGISTRY).build();

        }catch (Exception e){
            throw new InsertException(ERROR_REGISTRY);
        }
    }

    private BankEntity toBankEntity(BankEntityReq bankEntityReq) {
        return BankEntity.builder()
                .name(bankEntityReq.getName())
                .internationalCode(bankEntityReq.getInternationalCode())
                .branches(toBranches(bankEntityReq.getBranches()))
                .build();
    }

    private List<Branch> toBranches(List<BranchReq> branches) {
        return branches.stream().map(branchReq -> Branch.builder()
                .code(branchReq.getCode())
                .name(branchReq.getName())
                .uniqueKey(UUID.randomUUID().toString())
                .creationDate(LocalDate.now())
                .state(branchReq.getState())
                .emailAdress(branchReq.getEmailAdress())
                .phoneNumber(branchReq.getPhoneNumber())
                .line1(branchReq.getLine1())
                .line2(branchReq.getLine2())
                .latitude(branchReq.getLatitude())
                .longitude(branchReq.getLongitude())
                .build()).collect(Collectors.toList());
    }

}
