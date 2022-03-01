package com.project.kcs.repository.userterms;

import com.project.kcs.entity.Terms;
import com.project.kcs.entity.User;
import com.project.kcs.entity.UserTerms;
import com.project.kcs.entity.constant.*;
import com.project.kcs.repository.terms.TermsRepository;
import com.project.kcs.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTermsRepositoryTest {

    @Autowired
    UserTermsRepository userTermsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TermsRepository termsRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeAll
    public void setUp() {
        userRepository.save(createUser());
        termsRepository.save(createTerms());
    }

    @Test
    public void 약관동의() {

        // given
        User user = userRepository.findUserByuserId("userA")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자가 존재하지 않습니다.");
                });

        List<Terms> termsList = termsRepository.findAll()
                .stream()
                .filter(terms -> terms.available())
                .collect(Collectors.toList());

        // when
        termsList.stream()
                .forEach(terms -> {

                    UserTerms userTerms = UserTerms.builder()
                            .user(user)
                            .terms(terms)
                            .availableTime(LocalDateTime.now())
                            .termsAgreeType(TermsAgreeType.AGREE)
                            .build();

                    userTermsRepository.save(userTerms);

                });

        assertThat(userTermsRepository.findAll().size()).isEqualTo(termsList.size());
    }

    @DisplayName("동의한 약관을 미동의로 변경")
    @Test
    public void 약관수정() {

        // given
        User user = userRepository.findUserByuserId("userA")
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자가 존재하지 않습니다.");
                });

        List<Terms> termsList = termsRepository.findAll()
                .stream()
                .filter(terms -> terms.available())
                .collect(Collectors.toList());

        termsList.stream()
                .forEach(terms -> {

                    UserTerms userTerms = UserTerms.builder()
                            .user(user)
                            .terms(terms)
                            .availableTime(LocalDateTime.now())
                            .termsAgreeType(TermsAgreeType.AGREE)
                            .build();

                    userTermsRepository.save(userTerms);

                });

        UserTerms userTerms = userTermsRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);

        userTerms.modifyTermsAgreeType(TermsAgreeType.DISAGREE);

        entityManager.flush();
        entityManager.clear();

        UserTerms modifyUserTerms = userTermsRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);

        assertThat(modifyUserTerms.getTermsAgreeType()).isEqualTo(TermsAgreeType.DISAGREE);

    }

    private Terms createTerms() {
        return Terms.builder()
                .type(TermsType.SCRAP)
                .title("스크래핑동의약관")
                .content("스크래핑하는것에동의하시나요?")
                .status(TermsStatus.USE)
                .build();
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

}