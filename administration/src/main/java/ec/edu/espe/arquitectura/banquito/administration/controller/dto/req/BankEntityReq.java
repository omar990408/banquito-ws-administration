package ec.edu.espe.arquitectura.banquito.administration.controller.dto.req;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BankEntityReq{
    private String name;
    private String internationalCode;
    private List<BranchReq> branches;
}
