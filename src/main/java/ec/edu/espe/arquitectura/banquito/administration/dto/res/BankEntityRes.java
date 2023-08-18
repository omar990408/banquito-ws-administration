package ec.edu.espe.arquitectura.banquito.administration.dto.res;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BankEntityRes {
    private String name;
    private String internationalCode;
    private List<BranchRes> branches;
}
