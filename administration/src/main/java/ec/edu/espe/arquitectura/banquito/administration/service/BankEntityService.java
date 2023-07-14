package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.BankEntityReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BaseRes;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BranchRes;
import ec.edu.espe.arquitectura.banquito.administration.exception.InsertException;
import ec.edu.espe.arquitectura.banquito.administration.exception.NotFoundException;
import ec.edu.espe.arquitectura.banquito.administration.exception.UpdateException;
import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import ec.edu.espe.arquitectura.banquito.administration.repository.BankEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static ec.edu.espe.arquitectura.banquito.administration.constant.MessageConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public BaseRes update (String iC, BankEntityReq bankEntityReq) throws UpdateException {
        try{
            BankEntity bankEntity = getBankEntityByInternationalCode(iC);
            bankEntity.setName(bankEntityReq.getName());
            if (bankEntityReq.getBranches() != null){
                for (Branch branch : bankEntity.getBranches()) {
                    for (BranchReq branchReq : bankEntityReq.getBranches()) {
                        if(branch.getCode().equals(branchReq.getCode())){
                            if (branchReq.getName() != null) {
                                branch.setName(branchReq.getName());
                            }
                            if (branchReq.getState() != null) {
                                branch.setState(branchReq.getState());
                            }
                            if (branchReq.getEmailAddress() != null) {
                                branch.setEmailAddress(branchReq.getEmailAddress());
                            }
                            if (branchReq.getPhoneNumber() != null) {
                                branch.setPhoneNumber(branchReq.getPhoneNumber());
                            }
                            if (branchReq.getLine1() != null) {
                                branch.setLine1(branchReq.getLine1());
                            }
                            if (branchReq.getLine2() != null) {
                                branch.setLine2(branchReq.getLine2());
                            }
                            if (branchReq.getLatitude() != null) {
                                branch.setLatitude(branchReq.getLatitude());
                            }
                            if (branchReq.getLongitude() != null) {
                                branch.setLongitude(branchReq.getLongitude());
                            }
                        }
                    }
                }
            }
            bankEntityRepository.save(bankEntity);
            return BaseRes.builder().message(SUCCESS_UPDATE).build();
        }catch (Exception e){
            throw new UpdateException(ERROR_UPDATE);
        }
    }




    public BankEntityRes findBankEntityByIntCode(String internationalCode) throws NotFoundException {
        return toBankEntityRes(getBankEntityByInternationalCode(internationalCode));
    }

    public BaseRes addBranch(String internationalCode, BranchReq branchReq) throws UpdateException {
        try{
            BankEntity bankEntity = getBankEntityByInternationalCode(internationalCode);
            for (Branch branch : bankEntity.getBranches()) {
                if (branch.getCode().equals(branchReq.getCode())){
                    throw new UpdateException(ERROR_REGISTRY);
                }
            }
            Branch branch = toBranch(branchReq);
            bankEntity.getBranches().add(branch);
            bankEntityRepository.save(bankEntity);
            return BaseRes.builder().message(SUCCESS_REGISTRY).build();
        }catch (Exception e){
            throw new UpdateException(ERROR_REGISTRY);
        }
    }

    private Branch toBranch(BranchReq branchReq) {
        return Branch.builder()
                .code(branchReq.getCode())
                .name(branchReq.getName())
                .uniqueKey(UUID.randomUUID().toString())
                .creationDate(LocalDate.now())
                .state("ACT")
                .emailAddress(branchReq.getEmailAddress())
                .phoneNumber(branchReq.getPhoneNumber())
                .line1(branchReq.getLine1())
                .line2(branchReq.getLine2())
                .latitude(branchReq.getLatitude())
                .longitude(branchReq.getLongitude())
                .build();
    }

    private BankEntity getBankEntityByInternationalCode(String internationalCode) throws NotFoundException {
        Optional<BankEntity> bankEntity = bankEntityRepository.findByInternationalCode(internationalCode);
        if (bankEntity.isPresent()) {
            return bankEntity.get();
        }else {
            throw new NotFoundException(NOT_FOUND_ID + internationalCode);
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
                .state("ACT")
                .emailAddress(branchReq.getEmailAddress())
                .phoneNumber(branchReq.getPhoneNumber())
                .line1(branchReq.getLine1())
                .line2(branchReq.getLine2())
                .latitude(branchReq.getLatitude())
                .longitude(branchReq.getLongitude())
                .build()).collect(Collectors.toList());
    }
    private BankEntityRes toBankEntityRes(BankEntity bankEntity) {
        return BankEntityRes.builder()
                .name(bankEntity.getName())
                .internationalCode(bankEntity.getInternationalCode())
                .branches(toBranchesRes(bankEntity.getBranches()))
                .build();
    }

    private List<BranchRes> toBranchesRes(List<Branch> branches) {
        return branches.stream().map(branch -> BranchRes.builder()
                .code(branch.getCode())
                .name(branch.getName())
                .emailAddress(branch.getEmailAddress())
                .phoneNumber(branch.getPhoneNumber())
                .line1(branch.getLine1())
                .line2(branch.getLine2())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .state(branch.getState())
                .build()).collect(Collectors.toList());
    }
}
