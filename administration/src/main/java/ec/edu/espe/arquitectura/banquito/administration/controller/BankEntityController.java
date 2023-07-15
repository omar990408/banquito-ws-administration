package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.BankEntityReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import ec.edu.espe.arquitectura.banquito.administration.service.BankEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bankEntity")
public class BankEntityController {
    private final BankEntityService bankEntityService;

    public BankEntityController(BankEntityService bankEntityService) {
        this.bankEntityService = bankEntityService;
    }

    @PostMapping("/create")
    public ResponseEntity<BankEntity> create (@RequestBody BankEntityReq bankEntityReq) {
        try{
            BankEntity bankEntity = this.bankEntityService.create(bankEntityReq);
            return ResponseEntity.ok().body(bankEntity);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BankEntity> update(@PathVariable("id") String internationalCode, @RequestBody BankEntityReq bankEntityReq){
        try {
            BankEntity bankEntity = this.bankEntityService.update(internationalCode, bankEntityReq);
            return ResponseEntity.ok().body(bankEntity);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/addBranch/{id}")
    public ResponseEntity<BankEntity> addBranch(@PathVariable("id") String internationalCode, @RequestBody BranchReq branchReq){
        try {
            BankEntity bankEntity = this.bankEntityService.addBranch(internationalCode, branchReq);
            return ResponseEntity.ok().body(bankEntity);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/find/{internationalCode}")
    public ResponseEntity<BankEntityRes> findBankEntityByIntCode(@PathVariable String internationalCode){
        BankEntityRes bankEntityRes = this.bankEntityService.findBankEntityByInternationalCode(internationalCode);
        return ResponseEntity.ok().body(bankEntityRes);
    }
}
