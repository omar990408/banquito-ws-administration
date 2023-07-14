package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.dto.req.BankEntityReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.req.BranchReq;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BankEntityRes;
import ec.edu.espe.arquitectura.banquito.administration.dto.res.BaseRes;
import ec.edu.espe.arquitectura.banquito.administration.exception.InsertException;
import ec.edu.espe.arquitectura.banquito.administration.exception.NotFoundException;
import ec.edu.espe.arquitectura.banquito.administration.exception.UpdateException;
import ec.edu.espe.arquitectura.banquito.administration.service.BankEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankEntity")
@RequiredArgsConstructor
public class BankEntityController {
    private final BankEntityService bankEntityService;

    @PostMapping("/create")
    public ResponseEntity<BaseRes> create (@RequestBody BankEntityReq bankEntityReq) throws InsertException {
        return ResponseEntity.ok().body(bankEntityService.createBankEntity(bankEntityReq));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseRes> update(@PathVariable("id") String internationalCode, @RequestBody BankEntityReq bankEntityReq) throws UpdateException {
        return ResponseEntity.ok().body(bankEntityService.update(internationalCode, bankEntityReq));
    }

    @PutMapping("/addBranch/{id}")
    public ResponseEntity<BaseRes> addBranch(@PathVariable("id") String internationalCode, @RequestBody BranchReq branchReq) throws UpdateException {
        return ResponseEntity.ok().body(bankEntityService.addBranch(internationalCode, branchReq));
    }

    @GetMapping("/find/{internationalCode}")
    public ResponseEntity<BankEntityRes> findBankEntityByIntCode(@PathVariable String internationalCode) throws NotFoundException {
        return ResponseEntity.ok().body(bankEntityService.findBankEntityByIntCode(internationalCode));
    }
}
