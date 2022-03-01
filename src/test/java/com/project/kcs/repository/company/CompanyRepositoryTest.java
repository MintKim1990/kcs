package com.project.kcs.repository.company;

import com.project.kcs.entity.Company;
import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.repository.user.UserRepository;
import com.project.kcs.request.company.ModifyCompanyRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeAll
    public void setUp() {
        userRepository.save(createUser());
        userRepository.save(createUser2());
    }

    @AfterAll
    public void clear(){
        userRepository.deleteAll();
    }

    @DisplayName("사업장등록")
    @Test
    public void 사업장등록() {

        // given
        User user = userRepository.findUserByuserId("userA")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자가 존재하지 않습니다.");
                });

        Company company = createCompanyByBusinessNumber("7348800358", user);

        // when
        companyRepository.save(company);

        // then
        assertThat(company.getKey()).isNotNull();
    }

    @DisplayName("사업장 등록시 동일한 사용자가 동일한 사업장을 등록할 수 없다.")
    @Test
    public void 유저동일사업장등록체크() {
        // given
        User user = userRepository.findUserByuserId("userA")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자가 존재하지 않습니다.");
                });

        Company company = createCompanyByBusinessNumber("7348800358", user);
        Company company2 = createCompanyByBusinessNumber("7348800358", user);

        // when
        companyRepository.save(company);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            companyRepository.save(company2);
        });
    }

    @DisplayName("사업장 등록시 이미 등록된 사업장에 다른 대표자가 등록시 등록할 수 없다.")
    @Test
    public void 공동대표자체크() {
        // given
        User userA = userRepository.findUserByuserId("userA")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자가 존재하지 않습니다.");
                });

        User userB = userRepository.findUserByuserId("userB")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자가 존재하지 않습니다.");
                });

        Company company = createCompanyByBusinessNumber("7348800358", userA);
        Company company2 = createCompanyByBusinessNumber("7348800358", userB);

        // when
        companyRepository.save(company);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            companyRepository.save(company2);
        });
    }

    @DisplayName("사업장정보변경")
    @Test
    public void 사업장정보변경() {

        // given
        User user = userRepository.findUserByuserId("userA")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자가 존재하지 않습니다.");
                });

        Company company = createCompanyByBusinessNumber("7348800358", user);

        // when
        companyRepository.save(company);

        ModifyCompanyRequest modifyCompanyRequest = new ModifyCompanyRequest(
                "테스트사업장",
                "",
                ""
        );

        company.ModifyCompanyInfo(modifyCompanyRequest);

        entityManager.flush();
        entityManager.clear();

        Company findCompany = companyRepository.findCompanyBybusinessNumber("7348800358")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사업장정보가 조회되지 않습니다.");
                });

        // then
        assertThat(findCompany.getCompanyName()).isEqualTo("테스트사업장");
        assertThat(findCompany.getCorporationNumber()).isEqualTo("1111111111111");
        assertThat(findCompany.getCompanyTelephone()).isEqualTo("0211112222");
    }

    @DisplayName("사업장정보삭제")
    @Test
    public void 사업장정보삭제() {

        // given
        User user = userRepository.findUserByuserId("userA")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자가 존재하지 않습니다.");
                });

        Company company = createCompanyByBusinessNumber("7348800358", user);

        companyRepository.save(company);

        // when
        companyRepository.deleteById(company.getKey());

        Optional<Company> findCompany = companyRepository.findCompanyBybusinessNumber("7348800358");

        assertThat(findCompany.isPresent()).isEqualTo(false);
    }

    private Company createCompanyByBusinessNumber(String businessNumber, User user) {
        Company company = Company.builder()
                .businessNumber(businessNumber)
                .companyName("한국신용데이터")
                .companyTelephone("0211112222")
                .corporationNumber("1111111111111")
                .foundationDate("20220220")
                .ownerName("대표자")
                .user(user)
                .build();
        return company;
    }

    private User createUser() {
        return User.builder()
                .userId("userA")
                .identificationInfo("testci")
                .userName("유저이름")
                .birthDate("19900703")
                .gender("1")
                .name("김민태")
                .hptel("01071656293")
                .userStatus(UserStatus.USE)
                .certificationType(CertificationType.KAKAO)
                .build();
    }

    private User createUser2() {
        return User.builder()
                .userId("userB")
                .identificationInfo("testci2")
                .userName("유저이름")
                .birthDate("19900703")
                .gender("1")
                .name("김민태")
                .hptel("01071656293")
                .userStatus(UserStatus.USE)
                .certificationType(CertificationType.KAKAO)
                .build();
    }

}