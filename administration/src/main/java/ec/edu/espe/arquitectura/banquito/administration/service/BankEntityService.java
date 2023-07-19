package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BranchRes;
import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import ec.edu.espe.arquitectura.banquito.administration.repository.BankEntityRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.BranchRepository;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.BankEntityMapper;
import ec.edu.espe.arquitectura.banquito.administration.service.mapper.BranchMapper;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankEntityService {
    private final BankEntityRepository bankEntityRepository;
    private final BankEntityMapper bankEntityMapper;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;


    public BankEntityService(BankEntityRepository bankEntityRepository, BankEntityMapper bankEntityMapper, BranchRepository branchRepository, BranchMapper branchMapper) {
        this.bankEntityRepository = bankEntityRepository;
        this.bankEntityMapper = bankEntityMapper;
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    private BankEntity getBankEntityByInternationalCode(String internationalCode){
        Optional<BankEntity> bankEntity = bankEntityRepository.findByInternationalCode(internationalCode);
        if (bankEntity.isPresent()) {
            return bankEntity.get();
        }else {
            throw new RuntimeException("La entidad Bancaria con código: "+internationalCode+" no existe");
        }
    }

    public BankEntityRes findBankEntityByInternationalCode(String internationalCode) {
        return this.bankEntityMapper.toBankEntityRes(getBankEntityByInternationalCode(internationalCode));
    }


    public Branch createBranch(BranchReq branchReq){
        Optional<Branch> branchTmp = this.branchRepository.findByCode(branchReq.getCode());
        if(branchTmp.isPresent()){
            throw new RuntimeException("La sucursal con código: "+branchReq.getCode()+" ya existe");
        }else{
            Branch branch = this.branchMapper.toBranch(branchReq);
            branch.setState("ACT");
            branch.setCreationDate(LocalDate.now());
            branch.setUniqueKey(UUID.randomUUID().toString());
            return this.branchRepository.save(branch);
        }
    }


    public Branch updateBranch(String code, BranchReq branchReq){
        Branch branch = getBranchByCode(code);
        this.branchMapper.updateBranch(branchReq, branch);
        return this.branchRepository.save(branch);
    }


    public Branch deleteBranch(String code){
        Branch branch = getBranchByCode(code);
        branch.setState("INA");
        return this.branchRepository.save(branch);
    }

    public BranchRes findBranchByCode(String code){
        Branch branch = getBranchByCode(code);
        return this.branchMapper.toBranchRes(branch);
    }

    public List<BranchRes> findAllBranchesByState(String state){
        List<Branch> branches = this.branchRepository.findAllByStateContaining(state);
        return this.branchMapper.toBranchRes(branches);
    }

    private Branch getBranchByCode(String code) {
        Optional<Branch> branch = this.branchRepository.findByCode(code);
        if(branch.isPresent()){
            return branch.get();
        }else{
            throw new RuntimeException("No existe la sucursal con código: "+code);
        }
    }
}
