package com.project.kcs.service.scrapcredential;

import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.constant.CredentialType;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.request.scrapcredential.InsertScrapCredentialRequest;
import com.project.kcs.response.scrapcredential.InsertScrapCredentialResponse;
import com.project.kcs.response.scrapcredential.SearchScrapCredentialResponse;
import com.project.kcs.service.user.UserService;
import com.project.kcs.utils.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ScrapCredentialServiceTest {

    @Autowired
    ScrapCredentialService scrapCredentialService;

    @Autowired
    UserService userService;

    @Test
    public void 기관전체조회() {

        // given
        User user = createUser();

        userService.join(user);

        scrapCredentialService.registration(new InsertScrapCredentialRequest(
                CredentialType.ALLCREDIT,
                "테스트아이디",
                "password"
        ), user);

        scrapCredentialService.registration(new InsertScrapCredentialRequest(
                CredentialType.CREFIA,
                "테스트아이디",
                "password"
        ), user);

        scrapCredentialService.registration(new InsertScrapCredentialRequest(
                CredentialType.HOMETAX,
                "테스트아이디",
                "password"
        ), user);

        List<SearchScrapCredentialResponse> searchScrapCredentialResponseList = scrapCredentialService.findAll(user);

        searchScrapCredentialResponseList.stream().forEach(System.out::println);

        assertThat(searchScrapCredentialResponseList.size()).isEqualTo(3);
    }

    @DisplayName("동일한 기관을 등록하려할경우 에러가 발생해야 한다.")
    @Test
    public void 기관중복등록() {

        // given
        User user = createUser();

        userService.join(user);

        scrapCredentialService.registration(new InsertScrapCredentialRequest(
                CredentialType.ALLCREDIT,
                "테스트아이디",
                "password"
        ), user);

        // when then
        assertThrows(IllegalStateException.class, () -> {
            scrapCredentialService.registration(new InsertScrapCredentialRequest(
                    CredentialType.ALLCREDIT,
                    "테스트아이디",
                    "password"
            ), user);
        });

    }

    @Test
    public void 기관등록() {

        // given
        User user = createUser();

        userService.join(user);

        InsertScrapCredentialResponse insertScrapCredentialResponse = scrapCredentialService.registration(new InsertScrapCredentialRequest(
                CredentialType.ALLCREDIT,
                "테스트아이디",
                "password"
        ), user);


        assertThat(insertScrapCredentialResponse.getType()).isEqualTo(CredentialType.ALLCREDIT);
        assertThat(insertScrapCredentialResponse.getId()).isEqualTo("테스트아이디");
        assertThat(PasswordUtil.convertDecoding(insertScrapCredentialResponse.getPassword())).isEqualTo("password");
    }

    @Test
    public void 기관수정() {

        // given
        User user = createUser();

        userService.join(user);

        InsertScrapCredentialResponse insertScrapCredentialResponse = scrapCredentialService.registration(new InsertScrapCredentialRequest(
                CredentialType.ALLCREDIT,
                "테스트아이디",
                "password"
        ), user);

        scrapCredentialService.modifyCredentialInfo(insertScrapCredentialResponse.getKey(), "아이디를바꿀게요", "newpassword", user);

        SearchScrapCredentialResponse modifyScrapCredentialResponse = scrapCredentialService.findAll(user)
                .stream()
                .filter(searchScrapCredentialResponse -> searchScrapCredentialResponse.getType().equals(CredentialType.ALLCREDIT))
                .findAny()
                .get();

        assertThat(modifyScrapCredentialResponse.getType()).isEqualTo(CredentialType.ALLCREDIT);
        assertThat(modifyScrapCredentialResponse.getId()).isEqualTo("아이디를바꿀게요");
        assertThat(modifyScrapCredentialResponse.getPassword()).isEqualTo("newpassword");
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