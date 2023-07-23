package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BranchRes;
import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import ec.edu.espe.arquitectura.banquito.administration.service.BankEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bankEntity")
public class BankEntityController {
    private final BankEntityService bankEntityService;


    public BankEntityController(BankEntityService bankEntityService) {
        this.bankEntityService = bankEntityService;
    }

    @GetMapping("/find/{internationalCode}")
    public ResponseEntity<BankEntityRes> findBankEntityByIntCode(@PathVariable String internationalCode){
        BankEntityRes bankEntityRes = this.bankEntityService.findBankEntityByInternationalCode(internationalCode);
        return ResponseEntity.ok().body(bankEntityRes);
    }

    @PostMapping("/addBranch")
    public ResponseEntity<Branch> create(@RequestBody BranchReq branchReq){
        try{
            Branch branch = this.bankEntityService.createBranch(branchReq);
            return ResponseEntity.ok().body(branch);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateBranch/{code}")
    public ResponseEntity<Branch> update(@PathVariable("code") String code, @RequestBody BranchReq branchReq){
        try{
            Branch branch = this.bankEntityService.updateBranch(code, branchReq);
            return ResponseEntity.ok().body(branch);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("deleteBranch/{code}")
    public ResponseEntity<Branch> delete(@PathVariable("code") String code){
        try{
            Branch branch = this.bankEntityService.deleteBranch(code);
            return ResponseEntity.ok().body(branch);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findBranch/{code}")
    public ResponseEntity<BranchRes> findBranchByCode(@PathVariable String code){
        BranchRes branchRes = this.bankEntityService.findBranchByCode(code);
        return ResponseEntity.ok().body(branchRes);
    }

    @GetMapping("/findAllBranches/{state}")
    public ResponseEntity<List<BranchRes>> findAllbyState(@PathVariable String state){
        List<BranchRes> branchRes = this.bankEntityService.findAllBranchesByState(state);
        return ResponseEntity.ok().body(branchRes);
    }

    @GetMapping("/findBranches-location/{locationId}")
    public ResponseEntity<List<BranchRes>> findAllbyLocation(@PathVariable String locationId){
        List<BranchRes> branchRes = this.bankEntityService.findAllBranchesByLocationId(locationId);
        return ResponseEntity.ok().body(branchRes);
    }
}
