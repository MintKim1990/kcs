package com.project.kcs.service.company;

import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.request.company.ModifyCompanyRequest;
import com.project.kcs.request.company.InsertCompanyRequest;
import com.project.kcs.response.company.InsertCompanyResponse;
import com.project.kcs.response.company.SearchCompanyResponse;
import com.project.kcs.service.user.UserService;
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
class CompanyServiceTest {

    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    @Test
    public void 사업장등록() {

        // given
        User user = createUser();

        userService.join(user);

        // when
        InsertCompanyResponse insertCompanyResponse = companyService.registration(new InsertCompanyRequest(
                "7348800358",
                "20221231",
                "신용데이터",
                "1111111111111",
                "0211112222"
        ), user);

        // then
        assertThat(insertCompanyResponse.getBusinessNumber()).isEqualTo("7348800358");
        assertThat(insertCompanyResponse.getOwnerName()).isEqualTo("김민태");
        assertThat(insertCompanyResponse.getFoundationDate()).isEqualTo("20221231");
    }

    @DisplayName("유효하지 않는 사업장 정보를 등록하려했을때 에러가 발생해야 한다.")
    @Test
    public void 유효하지않은사업장등록() {

        // given
        User user = createUserByName("뷁뷁뷁");

        userService.join(user);

        // when then
        assertThrows(IllegalStateException.class, () -> {
            companyService.registration(new InsertCompanyRequest(
                    "7348800358",
                    "20221231",
                    "신용데이터",
                    "1111111111111",
                    "0211112222"
            ), user);
        });

    }

    @DisplayName("동일회원이 등록된 사업장을 등록하려 할 경우 에러가 발생해야 한다.")
    @Test
    public void 동일사업장중복등록() {
        // given
        User user = createUser();

        userService.join(user);

        companyService.registration(new InsertCompanyRequest(
                "7348800358",
                "20221231",
                "신용데이터",
                "1111111111111",
                "0211112222"
        ), user);

        // when then
        assertThrows(IllegalStateException.class, () -> {
            companyService.registration(new InsertCompanyRequest(
                    "7348800358",
                    "20221231",
                    "신용데이터",
                    "1111111111111",
                    "0211112222"
            ), user);
        });
    }

    @DisplayName("공동대표자가 이미 등록된 사업장을 등록하려할경우 등록이 불가해야 한다.")
    @Test
    public void 공동대표자동일사업장중복등록() {
        // given
        User user = createUser();

        userService.join(user);

        companyService.registration(new InsertCompanyRequest(
                "7348800358",
                "20221231",
                "신용데이터",
                "1111111111111",
                "0211112222"
        ), user);

        User user2 = createUserByUserIdAndIdentificationInfo("userB", "testCI2");

        userService.join(user2);

        // when then
        assertThrows(IllegalStateException.class, () -> {
            companyService.registration(new InsertCompanyRequest(
                    "7348800358",
                    "20221231",
                    "신용데이터",
                    "1111111111111",
                    "0211112222"
            ), user2);
        });
    }

    @Test
    public void 사업장조회() {

        // given
        User user = createUser();

        userService.join(user);

        InsertCompanyResponse insertCompanyResponse = companyService.registration(new InsertCompanyRequest(
                "7348800358",
                "20221231",
                "신용데이터",
                "1111111111111",
                "0211112222"
        ), user);

        // when
        SearchCompanyResponse searchCompanyResponse = companyService.findOne(insertCompanyResponse.getKey(), user);

        assertThat(insertCompanyResponse.getBusinessNumber()).isEqualTo("7348800358");
        assertThat(insertCompanyResponse.getOwnerName()).isEqualTo("김민태");
        assertThat(insertCompanyResponse.getFoundationDate()).isEqualTo("20221231");
    }

    @Test
    public void 사용자사업장전체조회() {

        // given
        User user = createUser();

        userService.join(user);

        companyService.registration(new InsertCompanyRequest(
                "7348800358",
                "20221231",
                "신용데이터",
                "1111111111111",
                "0211112222"
        ), user);

        companyService.registration(new InsertCompanyRequest(
                "7348800359",
                "20221231",
                "신용데이터",
                "1111111111111",
                "0211112222"
        ), user);

        // when
        List<SearchCompanyResponse> searchCompanyResponseList = companyService.findAll(user);

        searchCompanyResponseList.stream().forEach(System.out::println);

        // then
        assertThat(searchCompanyResponseList.size()).isEqualTo(2);

    }

    @Test
    public void 사업장수정() {

        // given
        User user = createUser();

        userService.join(user);

        InsertCompanyResponse insertCompanyResponse = companyService.registration(new InsertCompanyRequest(
                "7348800358",
                "20221231",
                "신용데이터",
                "1111111111111",
                "0211112222"
        ), user);

        // when
        companyService.modify(new ModifyCompanyRequest(
                "회시이름을변경해요",
                "1111111122222",
                "02123445667"
        ), insertCompanyResponse.getKey(), user);

        SearchCompanyResponse searchCompanyResponse = companyService.findOne(insertCompanyResponse.getKey(), user);

        // then
        assertThat(searchCompanyResponse.getCompanyName()).isEqualTo("회시이름을변경해요");
        assertThat(searchCompanyResponse.getCorporationNumber()).isEqualTo("1111111122222");
        assertThat(searchCompanyResponse.getCompanyTelephone()).isEqualTo("02123445667");

    }

    @Test
    public void 사업장삭제() {
        // given
        User user = createUser();

        userService.join(user);

        InsertCompanyResponse insertCompanyResponse = companyService.registration(new InsertCompanyRequest(
                "7348800358",
                "20221231",
                "신용데이터",
                "1111111111111",
                "0211112222"
        ), user);

        // when
        companyService.delete(insertCompanyResponse.getKey(), user);

        // then
        SearchCompanyResponse searchCompanyResponse = companyService.findOne(insertCompanyResponse.getKey(), user);

        assertThat(searchCompanyResponse).isNull();
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

    private User createUserByName(String Name) {
        return User.builder()
                .userId("userA")
                .identificationInfo("testci")
                .userName("유저이름")
                .birthDate("19900703")
                .gender("1")
                .name(Name)
                .hptel("01071656293")
                .userStatus(UserStatus.USE)
                .certificationType(CertificationType.KAKAO)
                .build();
    }

    private User createUserByUserIdAndIdentificationInfo(String userId, String identificationInfo) {
        return User.builder()
                .userId(userId)
                .identificationInfo(identificationInfo)
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