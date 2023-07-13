package ec.edu.espe.arquitectura.banquito.administration.controller;

import ec.edu.espe.arquitectura.banquito.administration.controller.dto.req.BankEntityReq;
import ec.edu.espe.arquitectura.banquito.administration.controller.dto.res.BaseRes;
import ec.edu.espe.arquitectura.banquito.administration.exception.InsertException;
import ec.edu.espe.arquitectura.banquito.administration.model.BankEntity;
import ec.edu.espe.arquitectura.banquito.administration.service.BankEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ec.edu.espe.arquitectura.banquito.administration.constant.MessageConstant.ERROR_REGISTRY;

@RestController
@RequestMapping("/bankEntity")
@RequiredArgsConstructor
public class BankEntityController {
    private final BankEntityService bankEntityService;

    @PostMapping("/create")
    public ResponseEntity<BaseRes> create (@RequestBody BankEntityReq bankEntityReq) throws InsertException {
        return ResponseEntity.ok().body(bankEntityService.createBankEntity(bankEntityReq));
    }
}
