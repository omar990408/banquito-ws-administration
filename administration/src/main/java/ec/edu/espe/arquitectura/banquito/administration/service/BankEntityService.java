package ec.edu.espe.arquitectura.banquito.administration.service;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BranchRes;
import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import ec.edu.espe.arquitectura.banquito.administration.model.GeoLocation;
import ec.edu.espe.arquitectura.banquito.administration.repository.BankEntityRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.BranchRepository;
import ec.edu.espe.arquitectura.banquito.administration.repository.GeoLocationRepository;
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

    private final GeoLocationRepository geoLocationRepository;


    public BankEntityService(BankEntityRepository bankEntityRepository, BankEntityMapper bankEntityMapper, BranchRepository branchRepository, BranchMapper branchMapper, GeoLocationRepository geoLocationRepository) {
        this.bankEntityRepository = bankEntityRepository;
        this.bankEntityMapper = bankEntityMapper;
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.geoLocationRepository = geoLocationRepository;
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
        BranchRes branchRes = this.branchMapper.toBranchRes(branch);
        branchRes.setLocationName(getGeoLocationNameByUuid(branch.getLocationId()));
        return branchRes;
    }

    public List<BranchRes> findAllBranchesByState(String state){
        List<Branch> branches = this.branchRepository.findAllByStateContaining(state);
        List<BranchRes> branchRes = this.branchMapper.toBranchRes(branches);
        for (BranchRes branch: branchRes) {
            branch.setLocationName(getGeoLocationNameByUuid(branch.getLocationId()));
        }
        return branchRes;
    }

    public List<BranchRes> findAllBranchesByLocationId(String locationId){
        List<Branch> branches = this.branchRepository.findByLocationId(locationId);
        List<BranchRes> branchRes = this.branchMapper.toBranchRes(branches);
        for (BranchRes branch: branchRes) {
            branch.setLocationName(getGeoLocationNameByUuid(branch.getLocationId()));
        }
        return branchRes;
    }

    public List<BranchRes> findAllBranchesByLocationIdAndState(String locationId, String state){
        List<Branch> branches = this.branchRepository.findByLocationIdAndState(locationId, state);
        List<BranchRes> branchRes = this.branchMapper.toBranchRes(branches);
        for (BranchRes branch: branchRes) {
            branch.setLocationName(getGeoLocationNameByUuid(branch.getLocationId()));
        }
        return branchRes;
    }
    private Branch getBranchByCode(String code) {
        Optional<Branch> branch = this.branchRepository.findByCode(code);
        if(branch.isPresent()){
            return branch.get();
        }else{
            throw new RuntimeException("No existe la sucursal con código: "+code);
        }
    }

    private String getGeoLocationNameByUuid(String uuid) {
        Optional<GeoLocation> geoLocation = this.geoLocationRepository.findByUuid(uuid);
        if(geoLocation.isPresent()){
            return geoLocation.get().getName();
        }else{
            throw new RuntimeException("No existe la locación con código: "+uuid);
        }
    }
}
