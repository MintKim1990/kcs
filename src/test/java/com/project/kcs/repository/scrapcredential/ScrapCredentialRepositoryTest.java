package com.project.kcs.repository.scrapcredential;

import com.project.kcs.entity.ScrapCredential;
import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.constant.CredentialType;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.repository.user.UserRepository;
import com.project.kcs.utils.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScrapCredentialRepositoryTest {

    @Autowired
    private ScrapCredentialRepository scrapCredentialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void 사이트인증정보등록() {

        User user = createUser();

        userRepository.save(user);

        ScrapCredential scrapCredential = ScrapCredential.builder()
                .type(CredentialType.CREFIA)
                .id("testCrefia")
                .password(PasswordUtil.convertEncoding("password"))
                .user(user)
                .build();

        scrapCredentialRepository.save(scrapCredential);

        assertThat(scrapCredential.getKey()).isNotNull();
    }

    @Test
    public void 사이트인증정보수정() {

        User user = createUser();

        userRepository.save(user);

        ScrapCredential scrapCredential = ScrapCredential.builder()
                .type(CredentialType.CREFIA)
                .id("testCrefia")
                .password(PasswordUtil.convertEncoding("password"))
                .user(user)
                .build();

        scrapCredentialRepository.save(scrapCredential);

        scrapCredential.modifyId("test");
        scrapCredential.modifyPassword(PasswordUtil.convertEncoding("newPassword"));

        entityManager.flush();
        entityManager.clear();

        ScrapCredential findScrapCredential = scrapCredentialRepository.findById(scrapCredential.getKey())
                .orElseThrow(() -> {
                    throw new IllegalStateException("인증정보가 존재하지 않습니다.");
                });

        assertThat(findScrapCredential.getId()).isEqualTo("test");
        assertThat(PasswordUtil.convertDecoding(findScrapCredential.getPassword())).isEqualTo("newPassword");
    }

    @Test
    public void 사이트인증정보삭제() {

        User user = createUser();

        userRepository.save(user);

        ScrapCredential scrapCredential = ScrapCredential.builder()
                .type(CredentialType.CREFIA)
                .id("testCrefia")
                .password(PasswordUtil.convertEncoding("password"))
                .user(user)
                .build();

        scrapCredentialRepository.save(scrapCredential);

        scrapCredentialRepository.deleteById(scrapCredential.getKey());

        Optional<ScrapCredential> findCredential = scrapCredentialRepository.findById(scrapCredential.getKey());

        assertThat(findCredential.isPresent()).isEqualTo(false);

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