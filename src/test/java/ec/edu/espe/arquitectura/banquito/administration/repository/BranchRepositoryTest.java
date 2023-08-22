package ec.edu.espe.arquitectura.banquito.administration.repository;

import ec.edu.espe.arquitectura.banquito.administration.model.Branch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@DataMongoTest
class BranchRepositoryTest {

    @Autowired
    private BranchRepository branchRepositoryUnderTest;
    private Branch branch;


    @BeforeEach
    void setUp() {
        branch = Branch.builder()
                .code("PCHQ")
                .bankEntityId("64bc71e6e9a5d42c0b3029d2")
                .locationId("f1cffe4f-0aaa-49af-82a4-bb1f50b9ac44")
                .code("CODE-TEST-"+UUID.randomUUID().toString())
                .name("Branch Test")
                .uniqueKey(UUID.randomUUID().toString())
                .state("ACT")
                .creationDate(LocalDate.now())
                .emailAddress("test@mail.com")
                .phoneNumber("0999999999")
                .line1("Line 1")
                .line2("Line 2")
                .latitude(BigDecimal.valueOf(12.12))
                .longitude(BigDecimal.valueOf(-12.12))
                .build();
    }
    @Test
    @Disabled
    void findByCode() {
//given
        branchRepositoryUnderTest.save(branch);
        //when
        Optional<Branch> result = branchRepositoryUnderTest.findByCode(branch.getCode());
        //then
        assertThat(result)
                .isPresent().
                hasValueSatisfying(bE -> {
                    assertThat(bE).usingRecursiveComparison().isEqualTo(branch);
                });
    }

    @Test
    @Disabled
    void findAllByStateContaining() {
        //give
        branchRepositoryUnderTest.save(branch);
        //when
        List<Branch> branches = branchRepositoryUnderTest.findAllByStateContaining(branch.getState());
        //then
        assertThat(branches)
                .isNotEmpty()
                .contains(branch);
    }

    @Test
    @Disabled
    void findByLocationId() {
        //give
        branchRepositoryUnderTest.save(branch);
        //when
        List<Branch> branches = branchRepositoryUnderTest.findByLocationId(branch.getLocationId());
        //then
        assertThat(branches)
                .isNotEmpty()
                .contains(branch);
    }

    @Test
    @Disabled
    void findByLocationIdAndState() {
        //give
        branchRepositoryUnderTest.save(branch);
        //when
        List<Branch> branches = branchRepositoryUnderTest.findByLocationIdAndState(branch.getLocationId(), branch.getState());
        //then
        assertThat(branches)
                .isNotEmpty()
                .contains(branch);
    }

    @AfterEach
    void deleteSetUp() {
      branchRepositoryUnderTest.delete(branch);
    }
}